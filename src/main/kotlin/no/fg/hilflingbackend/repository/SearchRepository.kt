package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.asc
import me.liuwj.ktorm.dsl.count
import me.liuwj.ktorm.dsl.desc
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.from
import me.liuwj.ktorm.dsl.greater
import me.liuwj.ktorm.dsl.greaterEq
import me.liuwj.ktorm.dsl.inList
import me.liuwj.ktorm.dsl.isNull
import me.liuwj.ktorm.dsl.leftJoin
import me.liuwj.ktorm.dsl.lessEq
import me.liuwj.ktorm.dsl.limit
import me.liuwj.ktorm.dsl.map
import me.liuwj.ktorm.dsl.orderBy
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.whereWithConditions
import me.liuwj.ktorm.entity.EntitySequence
import me.liuwj.ktorm.entity.drop
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.sorted
import me.liuwj.ktorm.entity.take
import me.liuwj.ktorm.entity.toList
import me.liuwj.ktorm.expression.ArgumentExpression
import me.liuwj.ktorm.expression.FunctionExpression
import me.liuwj.ktorm.expression.OrderByExpression
import me.liuwj.ktorm.schema.ColumnDeclaring
import me.liuwj.ktorm.schema.DoubleSqlType
import me.liuwj.ktorm.schema.VarcharSqlType
import no.fg.hilflingbackend.dto.ActiveFilters
import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.model.Categories
import no.fg.hilflingbackend.model.Motives
import no.fg.hilflingbackend.model.Photo
import no.fg.hilflingbackend.model.Photos
import no.fg.hilflingbackend.model.Places
import no.fg.hilflingbackend.model.motives
import no.fg.hilflingbackend.model.photos
import no.fg.hilflingbackend.model.toDto
import no.fg.hilflingbackend.valueobject.SearchSortField
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import no.fg.hilflingbackend.valueobject.SortDirection
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * Minimum pg_trgm word-similarity for a row to count as a fuzzy match.
 * pg_trgm's own default (word_similarity_threshold) is 0.6; we loosen it so
 * partial words and small typos still match. Ordering by score keeps the best
 * matches on top, so a permissive threshold only adds weak matches at the tail.
 */
const val SEARCH_SIMILARITY_THRESHOLD: Double = 0.3

@Repository
class SearchRepository(
  val database: Database,
) {
  private fun allowedSecurityLevels(userLevel: SecurityLevelType): List<String> =
    SecurityLevelType
      .values()
      .filter { it.ordinal >= userLevel.ordinal }
      .map { it.name }

  private fun direction(
    column: ColumnDeclaring<*>,
    sortDirection: SortDirection,
  ): OrderByExpression = if (sortDirection == SortDirection.ASC) column.asc() else column.desc()

  /**
   * PostgreSQL `word_similarity(query, target)`: how well [query] matches the
   * best-matching continuous substring of [target] (0.0–1.0). Preferred over
   * plain `similarity()` for search, where the query is usually much shorter
   * than the title. Backed by the pg_trgm extension + GIN index on the title
   * (both created in V1).
   */
  private fun wordSimilarity(
    query: String,
    target: ColumnDeclaring<String>,
  ): FunctionExpression<Double> =
    FunctionExpression(
      functionName = "word_similarity",
      arguments = listOf(ArgumentExpression(query, VarcharSqlType), target.asExpression()),
      sqlType = DoubleSqlType,
    )

  /** Fuzzy title match: keep rows whose word-similarity to [term] clears the threshold. */
  private fun titleMatches(term: String): ColumnDeclaring<Boolean> = wordSimilarity(term, Motives.title) greater SEARCH_SIMILARITY_THRESHOLD

  /**
   * Relevance is only meaningful when there is a term to score against, so an
   * absent sort field (or an explicit RELEVANCE with a blank term) falls back to
   * DATE_TAKEN; otherwise a term search defaults to RELEVANCE.
   */
  private fun resolveSort(
    sortField: SearchSortField?,
    term: String,
  ): SearchSortField =
    when {
      sortField != null && sortField != SearchSortField.RELEVANCE -> sortField
      term.isBlank() -> SearchSortField.DATE_TAKEN
      else -> SearchSortField.RELEVANCE
    }

  /**
   * Order clause for a resolved (non-null) sort field, always tie-broken by
   * [idColumn] for stable paging. [dateUploaded] differs between motives and
   * photos, so it is passed in.
   */
  private fun orderExpressions(
    sort: SearchSortField,
    sortDirection: SortDirection,
    term: String,
    dateUploaded: ColumnDeclaring<*>,
    idColumn: ColumnDeclaring<*>,
  ): List<OrderByExpression> =
    if (sort == SearchSortField.RELEVANCE) {
      listOf(wordSimilarity(term, Motives.title).desc(), idColumn.asc())
    } else {
      val column =
        when (sort) {
          SearchSortField.DATE_TAKEN -> Motives.date
          SearchSortField.DATE_UPLOADED -> dateUploaded
          SearchSortField.MOTIVE_TITLE -> Motives.title
          SearchSortField.CATEGORY -> Categories.name
          SearchSortField.PLACE -> Places.name
          SearchSortField.RELEVANCE -> Motives.title // unreachable: handled above
        }
      listOf(direction(column, sortDirection), idColumn.asc())
    }

  fun searchMotives(
    term: String,
    userLevel: SecurityLevelType,
    filters: ActiveFilters,
    sortField: SearchSortField?,
    sortDirection: SortDirection,
    page: Int,
    pageSize: Int,
  ): Page<MotiveDto> {
    val effectiveSort = resolveSort(sortField, term)
    val orders = orderExpressions(effectiveSort, sortDirection, term, Motives.dateCreated, Motives.id)

    val conditions = motiveConditions(term, userLevel, filters)

    // Filtering and sorting need the referenced category/place tables joined explicitly:
    // sorting/filtering on them through the entity-sequence auto-join is not resolved reliably,
    // so we drive paging with the query DSL and then hydrate the DTOs by id.
    val source =
      database
        .from(Motives)
        .leftJoin(Categories, on = Motives.categoryId eq Categories.id)
        .leftJoin(Places, on = Motives.placeId eq Places.id)

    val orderedIds: List<UUID> =
      source
        .select(Motives.id)
        .whereWithConditions { it += conditions }
        .orderBy(*orders.toTypedArray())
        .limit(page * pageSize, pageSize)
        .map { it[Motives.id]!! }

    val totalRecords =
      source
        .select(count(Motives.id))
        .whereWithConditions { it += conditions }
        .map { it.getInt(1) }
        .firstOrNull() ?: 0

    val dtosById =
      if (orderedIds.isEmpty()) {
        emptyMap()
      } else {
        database.motives
          .filter { it.id inList orderedIds }
          .toList()
          .associate { it.id to it.toDto() }
      }
    val motiveDtos = orderedIds.mapNotNull { dtosById[it] }

    return Page(
      page = page,
      pageSize = pageSize,
      totalRecords = totalRecords,
      currentList = motiveDtos,
    )
  }

  fun searchPictures(
    term: String,
    userLevel: SecurityLevelType,
    filters: ActiveFilters,
    sortField: SearchSortField?,
    sortDirection: SortDirection,
    page: Int,
    pageSize: Int,
  ): Page<PhotoDto> {
    val canAccessProd = userLevel.ordinal <= SecurityLevelType.HUSFOLK.ordinal
    var sequence: EntitySequence<Photo, Photos> =
      database.photos.filter {
        it.dateDeleted.isNull() and (it.securityLevel inList allowedSecurityLevels(userLevel))
      }
    sequence = applyPictureFilters(sequence, term, filters)

    val effectiveSort = resolveSort(sortField, term)
    val orders = orderExpressions(effectiveSort, sortDirection, term, Photos.dateCreated, Photos.id)

    val photoDtos =
      sequence
        .sorted { orders }
        .drop(page * pageSize)
        .take(pageSize)
        .toList()
        .map { photo ->
          val dto = photo.toDto()
          if (canAccessProd) dto else dto.copy(imageProd = null)
        }

    return Page(
      page = page,
      pageSize = pageSize,
      totalRecords = sequence.totalRecords,
      currentList = photoDtos,
    )
  }

  private fun motiveConditions(
    term: String,
    userLevel: SecurityLevelType,
    filters: ActiveFilters,
  ): List<ColumnDeclaring<Boolean>> {
    val conditions = ArrayList<ColumnDeclaring<Boolean>>()
    conditions += Motives.dateDeleted.isNull()
    conditions += Motives.securityLevel inList allowedSecurityLevels(userLevel)
    if (term.isNotBlank()) conditions += titleMatches(term)
    if (filters.placeIds.isNotEmpty()) conditions += Motives.placeId inList filters.placeIds
    if (filters.categoryIds.isNotEmpty()) conditions += Motives.categoryId inList filters.categoryIds
    if (filters.eventOwnerIds.isNotEmpty()) conditions += Motives.eventOwnerId inList filters.eventOwnerIds
    if (filters.albumIds.isNotEmpty()) conditions += Motives.albumId inList filters.albumIds
    if (filters.securityLevels.isNotEmpty()) conditions += Motives.securityLevel inList filters.securityLevels
    filters.dateFrom?.let { from -> conditions += Motives.date greaterEq from }
    filters.dateTo?.let { to -> conditions += Motives.date lessEq to }
    return conditions
  }

  private fun applyPictureFilters(
    sequence: EntitySequence<Photo, Photos>,
    term: String,
    filters: ActiveFilters,
  ): EntitySequence<Photo, Photos> {
    var seq = sequence
    if (term.isNotBlank()) seq = seq.filter { titleMatches(term) }
    if (filters.placeIds.isNotEmpty()) seq = seq.filter { Motives.placeId inList filters.placeIds }
    if (filters.categoryIds.isNotEmpty()) seq = seq.filter { Motives.categoryId inList filters.categoryIds }
    if (filters.eventOwnerIds.isNotEmpty()) seq = seq.filter { Motives.eventOwnerId inList filters.eventOwnerIds }
    if (filters.albumIds.isNotEmpty()) seq = seq.filter { Motives.albumId inList filters.albumIds }
    if (filters.securityLevels.isNotEmpty()) seq = seq.filter { it.securityLevel inList filters.securityLevels }
    filters.dateFrom?.let { from -> seq = seq.filter { Motives.date greaterEq from } }
    filters.dateTo?.let { to -> seq = seq.filter { Motives.date lessEq to } }
    return seq
  }
}

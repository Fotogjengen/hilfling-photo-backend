package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import no.fg.hilflingbackend.dto.ActiveFilters
import no.fg.hilflingbackend.dto.FilterSuggestionDto
import no.fg.hilflingbackend.dto.FilterSuggestionType
import no.fg.hilflingbackend.dto.FilterSuggestionsResponseDto
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.time.LocalDate
import java.util.UUID

@Repository
class SearchSuggestionsRepository(
  val database: Database,
) {
  private sealed class Param

  private data class Str(
    val v: String,
  ) : Param()

  private data class UuidArr(
    val v: List<UUID>,
  ) : Param()

  private data class StrArr(
    val v: List<String>,
  ) : Param()

  private data class DateVal(
    val v: LocalDate,
  ) : Param()

  private fun PreparedStatement.bindAll(
    ps: List<Param>,
    conn: Connection,
  ) = ps.forEachIndexed { i, p ->
    when (p) {
      is Str -> setString(i + 1, p.v)
      is UuidArr -> setArray(i + 1, conn.createArrayOf("uuid", p.v.map { it.toString() }.toTypedArray()))
      is StrArr -> setArray(i + 1, conn.createArrayOf("varchar", p.v.toTypedArray()))
      is DateVal -> setObject(i + 1, p.v)
    }
  }

  // Appends m.date range conditions to an EXISTS/WHERE clause and binds their params in order.
  private fun addDateConditions(
    conditions: MutableList<String>,
    dateFrom: LocalDate?,
    dateTo: LocalDate?,
    params: MutableList<Param>,
  ) {
    if (dateFrom != null) {
      conditions.add("m.date >= ?")
      params.add(DateVal(dateFrom))
    }
    if (dateTo != null) {
      conditions.add("m.date <= ?")
      params.add(DateVal(dateTo))
    }
  }

  // Adds score params and returns the SQL expression (2 params: term, term)
  private fun scoreExpr(
    col: String,
    params: MutableList<Param>,
    term: String,
  ): String {
    params.add(Str(term))
    params.add(Str(term))
    return "GREATEST(similarity($col, ?), CASE WHEN $col ILIKE '%' || ? || '%' THEN 0.3 ELSE 0 END)"
  }

  // Builds WHERE for a regular entity type. Adds exclusion and EXISTS params in the correct order.
  // alias         — table alias used in the outer query (e.g. "p" for place)
  // motiveJoinCol — the motive column that links to this entity (e.g. "place_id")
  // selectedIds   — already-active filter values to exclude from suggestions
  // otherUuidFilters / otherStringFilters — other active filters to apply in the EXISTS subquery
  private fun entityWhere(
    alias: String,
    motiveJoinCol: String,
    selectedIds: List<UUID>,
    otherUuidFilters: List<Pair<String, List<UUID>>>,
    otherStringFilters: List<Pair<String, List<String>>>,
    dateFrom: LocalDate?,
    dateTo: LocalDate?,
    allowedLevelsClause: String,
    params: MutableList<Param>,
  ): String {
    val conditions = mutableListOf("$alias.date_deleted IS NULL")

    if (selectedIds.isNotEmpty()) {
      conditions.add("$alias.id != ALL(?)")
      params.add(UuidArr(selectedIds))
    }

    val hasOtherFilters = otherUuidFilters.any { it.second.isNotEmpty() } || otherStringFilters.any { it.second.isNotEmpty() }
    val hasDateFilter = dateFrom != null || dateTo != null
    if (hasOtherFilters || hasDateFilter) {
      val existsConds =
        mutableListOf(
          "m.$motiveJoinCol = $alias.id",
          "m.date_deleted IS NULL",
          "m.security_level $allowedLevelsClause",
        )
      otherUuidFilters.forEach { (col, ids) ->
        if (ids.isNotEmpty()) {
          existsConds.add("m.$col = ANY(?)")
          params.add(UuidArr(ids))
        }
      }
      otherStringFilters.forEach { (col, vals) ->
        if (vals.isNotEmpty()) {
          existsConds.add("m.$col = ANY(?)")
          params.add(StrArr(vals))
        }
      }
      addDateConditions(existsConds, dateFrom, dateTo, params)
      conditions.add("EXISTS (SELECT 1 FROM motive m WHERE ${existsConds.joinToString(" AND ")})")
    }

    return conditions.joinToString(" AND ")
  }

  fun findFilterSuggestions(
    term: String,
    userLevel: SecurityLevelType,
    filters: ActiveFilters,
  ): FilterSuggestionsResponseDto {
    if (term.isBlank()) return FilterSuggestionsResponseDto(emptyList(), 0)

    val isAlleUser = userLevel == SecurityLevelType.ALLE
    // Levels at or below the user's own level — never suggest or expose a higher level.
    val allowedLevels =
      when (userLevel) {
        SecurityLevelType.FG -> listOf("FG", "HUSFOLK", "ALLE")
        SecurityLevelType.HUSFOLK -> listOf("HUSFOLK", "ALLE")
        SecurityLevelType.ALLE -> listOf("ALLE")
      }
    val allowedLevelsClause = "IN (${allowedLevels.joinToString(", ") { "'$it'" }})"

    val blocks = mutableListOf<String>()
    val params = mutableListOf<Param>()

    // Place
    run {
      val score = scoreExpr("p.name", params, term)
      val where =
        entityWhere(
          alias = "p",
          motiveJoinCol = "place_id",
          selectedIds = filters.placeIds,
          otherUuidFilters = listOf("category_id" to filters.categoryIds, "event_owner_id" to filters.eventOwnerIds, "album_id" to filters.albumIds),
          otherStringFilters = emptyList(),
          dateFrom = filters.dateFrom,
          dateTo = filters.dateTo,
          allowedLevelsClause,
          params,
        )
      blocks.add("SELECT 'PLACE' AS type, p.id::text AS id, p.name AS display_text, $score AS score FROM place p WHERE $where")
    }

    // Event owner
    run {
      val score = scoreExpr("e.name", params, term)
      val where =
        entityWhere(
          alias = "e",
          motiveJoinCol = "event_owner_id",
          selectedIds = filters.eventOwnerIds,
          otherUuidFilters = listOf("place_id" to filters.placeIds, "category_id" to filters.categoryIds, "album_id" to filters.albumIds),
          otherStringFilters = emptyList(),
          dateFrom = filters.dateFrom,
          dateTo = filters.dateTo,
          allowedLevelsClause,
          params,
        )
      blocks.add("SELECT 'EVENT_OWNER' AS type, e.id::text AS id, e.name AS display_text, $score AS score FROM event_owner e WHERE $where")
    }

    // Category
    run {
      val score = scoreExpr("c.name", params, term)
      val where =
        entityWhere(
          alias = "c",
          motiveJoinCol = "category_id",
          selectedIds = filters.categoryIds,
          otherUuidFilters = listOf("place_id" to filters.placeIds, "event_owner_id" to filters.eventOwnerIds, "album_id" to filters.albumIds),
          otherStringFilters = emptyList(),
          dateFrom = filters.dateFrom,
          dateTo = filters.dateTo,
          allowedLevelsClause,
          params,
        )
      blocks.add("SELECT 'CATEGORY' AS type, c.id::text AS id, c.name AS display_text, $score AS score FROM category c WHERE $where")
    }

    // Security level (non-ALLE users only)
    if (!isAlleUser) {
      val score = scoreExpr("sl.level", params, term)
      val conditions = mutableListOf<String>()

      if (filters.securityLevels.isNotEmpty()) {
        conditions.add("sl.level != ALL(?)")
        params.add(StrArr(filters.securityLevels))
      }

      val hasOtherFilters =
        filters.placeIds.isNotEmpty() || filters.categoryIds.isNotEmpty() ||
          filters.eventOwnerIds.isNotEmpty() || filters.albumIds.isNotEmpty()
      val hasDateFilter = filters.dateFrom != null || filters.dateTo != null
      if (hasOtherFilters || hasDateFilter) {
        val existsConds = mutableListOf("m.security_level = sl.level", "m.date_deleted IS NULL", "m.security_level $allowedLevelsClause")
        if (filters.placeIds.isNotEmpty()) {
          existsConds.add("m.place_id = ANY(?)")
          params.add(UuidArr(filters.placeIds))
        }
        if (filters.categoryIds.isNotEmpty()) {
          existsConds.add("m.category_id = ANY(?)")
          params.add(UuidArr(filters.categoryIds))
        }
        if (filters.eventOwnerIds.isNotEmpty()) {
          existsConds.add("m.event_owner_id = ANY(?)")
          params.add(UuidArr(filters.eventOwnerIds))
        }
        if (filters.albumIds.isNotEmpty()) {
          existsConds.add("m.album_id = ANY(?)")
          params.add(UuidArr(filters.albumIds))
        }
        addDateConditions(existsConds, filters.dateFrom, filters.dateTo, params)
        conditions.add("EXISTS (SELECT 1 FROM motive m WHERE ${existsConds.joinToString(" AND ")})")
      }

      val whereClause = if (conditions.isEmpty()) "" else "WHERE ${conditions.joinToString(" AND ")}"
      val levelValues = allowedLevels.joinToString(", ") { "('$it')" }
      blocks.add("SELECT 'SECURITY_LEVEL' AS type, sl.level AS id, sl.level AS display_text, $score AS score FROM (VALUES $levelValues) AS sl(level) $whereClause")
    }

    // Album (non-ALLE users only)
    if (!isAlleUser) {
      // 4 score params: name similarity, description similarity, name ilike, description ilike
      params.add(Str(term))
      params.add(Str(term))
      params.add(Str(term))
      params.add(Str(term))
      val score = """GREATEST(
            similarity(a.name, ?),
            similarity(COALESCE(a.description, ''), ?),
            CASE WHEN a.name ILIKE '%' || ? || '%' THEN 0.3 ELSE 0 END,
            CASE WHEN a.description ILIKE '%' || ? || '%' THEN 0.3 ELSE 0 END
          )"""
      val displayText = "CASE WHEN a.description IS NOT NULL THEN a.name || ' (' || a.description || ')' ELSE a.name END"
      val where =
        entityWhere(
          alias = "a",
          motiveJoinCol = "album_id",
          selectedIds = filters.albumIds,
          otherUuidFilters = listOf("place_id" to filters.placeIds, "category_id" to filters.categoryIds, "event_owner_id" to filters.eventOwnerIds),
          otherStringFilters = emptyList(),
          dateFrom = filters.dateFrom,
          dateTo = filters.dateTo,
          allowedLevelsClause,
          params,
        )
      blocks.add("SELECT 'ALBUM' AS type, a.id::text AS id, $displayText AS display_text, $score AS score FROM album a WHERE $where")
    }

    // Motive — apply all active filters directly
    run {
      val score = scoreExpr("m.title", params, term)
      val conditions = mutableListOf("m.date_deleted IS NULL", "m.security_level $allowedLevelsClause")
      if (filters.placeIds.isNotEmpty()) {
        conditions.add("m.place_id = ANY(?)")
        params.add(UuidArr(filters.placeIds))
      }
      if (filters.categoryIds.isNotEmpty()) {
        conditions.add("m.category_id = ANY(?)")
        params.add(UuidArr(filters.categoryIds))
      }
      if (filters.eventOwnerIds.isNotEmpty()) {
        conditions.add("m.event_owner_id = ANY(?)")
        params.add(UuidArr(filters.eventOwnerIds))
      }
      if (filters.albumIds.isNotEmpty()) {
        conditions.add("m.album_id = ANY(?)")
        params.add(UuidArr(filters.albumIds))
      }
      if (filters.securityLevels.isNotEmpty()) {
        conditions.add("m.security_level = ANY(?)")
        params.add(StrArr(filters.securityLevels))
      }
      addDateConditions(conditions, filters.dateFrom, filters.dateTo, params)
      blocks.add("SELECT 'MOTIVE' AS type, m.id::text AS id, m.title AS display_text, $score AS score FROM motive m WHERE ${conditions.joinToString(" AND ")}")
    }

    // grusom query, men ktorm kan ikke gjøre dette
    val sql =
      """
      SELECT type, id, display_text
      FROM (${blocks.joinToString(" UNION ALL ")}) combined
      WHERE score > 0.05
      ORDER BY score DESC
      LIMIT 5
      """.trimIndent()

    // Hidden motive count: motives matching the search term that fail at least one active filter
    val hasActiveFilters =
      filters.placeIds.isNotEmpty() || filters.categoryIds.isNotEmpty() ||
        filters.eventOwnerIds.isNotEmpty() || filters.albumIds.isNotEmpty() || filters.securityLevels.isNotEmpty() ||
        filters.dateFrom != null || filters.dateTo != null

    val countParams = mutableListOf<Param>()
    val countSql =
      if (hasActiveFilters) {
        countParams.add(Str(term))
        countParams.add(Str(term))
        val filterChecks = mutableListOf<String>()
        if (filters.placeIds.isNotEmpty()) {
          filterChecks.add("place_id = ANY(?)")
          countParams.add(UuidArr(filters.placeIds))
        }
        if (filters.categoryIds.isNotEmpty()) {
          filterChecks.add("category_id = ANY(?)")
          countParams.add(UuidArr(filters.categoryIds))
        }
        if (filters.eventOwnerIds.isNotEmpty()) {
          filterChecks.add("event_owner_id = ANY(?)")
          countParams.add(UuidArr(filters.eventOwnerIds))
        }
        if (filters.albumIds.isNotEmpty()) {
          filterChecks.add("album_id = ANY(?)")
          countParams.add(UuidArr(filters.albumIds))
        }
        if (filters.securityLevels.isNotEmpty()) {
          filterChecks.add("security_level = ANY(?)")
          countParams.add(StrArr(filters.securityLevels))
        }
        if (filters.dateFrom != null) {
          filterChecks.add("date >= ?")
          countParams.add(DateVal(filters.dateFrom))
        }
        if (filters.dateTo != null) {
          filterChecks.add("date <= ?")
          countParams.add(DateVal(filters.dateTo))
        }
        """
        SELECT COUNT(*) FROM motive
        WHERE date_deleted IS NULL
          AND security_level $allowedLevelsClause
          AND (similarity(title, ?) > 0.05 OR title ILIKE '%' || ? || '%')
          AND NOT (${filterChecks.joinToString(" AND ")})
        """.trimIndent()
      } else {
        null
      }

    return database.useConnection { conn ->
      val suggestions =
        conn.prepareStatement(sql).use { stmt ->
          stmt.bindAll(params, conn)
          stmt.executeQuery().use { rs ->
            val results = mutableListOf<FilterSuggestionDto>()
            while (rs.next()) {
              results +=
                FilterSuggestionDto(
                  type = FilterSuggestionType.valueOf(rs.getString("type")),
                  id = rs.getString("id"),
                  displayText = rs.getString("display_text"),
                )
            }
            results
          }
        }

      val hiddenMotiveCount =
        if (countSql != null) {
          conn.prepareStatement(countSql).use { stmt ->
            stmt.bindAll(countParams, conn)
            stmt.executeQuery().use { rs -> if (rs.next()) rs.getInt(1) else 0 }
          }
        } else {
          0
        }

      FilterSuggestionsResponseDto(suggestions, hiddenMotiveCount)
    }
  }
}

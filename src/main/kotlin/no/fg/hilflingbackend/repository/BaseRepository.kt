package no.fg.hilflingbackend.repository

import jakarta.persistence.EntityNotFoundException
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.forEach
import org.ktorm.dsl.from
import org.ktorm.dsl.isNull
import org.ktorm.dsl.limit
import org.ktorm.dsl.map
import org.ktorm.dsl.select
import org.ktorm.dsl.update
import org.ktorm.dsl.where
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.model.BaseModel
import no.fg.hilflingbackend.model.BaseTable
import java.time.LocalDate
import java.util.UUID

abstract class BaseRepository<E : BaseModel<E>, D, R>(
  val table: BaseTable<E>,
  val database: Database,
) : IRepository<E, D, R> {
  override fun findById(id: UUID): D? {
    val resultSet =
      database
        .from(table)
        .select()
        .where { (table.id eq id) and (table.dateDeleted.isNull()) }
    var t: D? = null
    resultSet.forEach { row -> t = convertToClass(row) }
    if (t == null) {
      throw EntityNotFoundException("Could not find Entity")
    }
    return t
  }

  override fun findAll(
    page: Int,
    pageSize: Int,
  ): Page<D> {
    val offset = page * pageSize
    val resultSet =
      database
        .from(table)
        .select()
        .where { table.dateDeleted.isNull() }
        .limit(offset, pageSize)
    return Page(
      page = page,
      pageSize = pageSize,
      totalRecords = resultSet.totalRecords,
      currentList = resultSet.map { row -> convertToClass(row) },
    )
  }

  override fun delete(id: UUID): Int =
    database.update(table) {
      set(it.dateDeleted, LocalDate.now())
      where {
        it.id eq id
      }
    }

  override fun patch(dto: R): D = throw NotImplementedError("Patch function is not implemented.")
}

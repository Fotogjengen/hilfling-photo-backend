package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.delete
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.forEach
import me.liuwj.ktorm.dsl.from
import me.liuwj.ktorm.dsl.limit
import me.liuwj.ktorm.dsl.map
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.where
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.model.BaseModel
import no.fg.hilflingbackend.model.BaseTable
import java.util.UUID

abstract class BaseRepository<E : BaseModel<E>, D>(val table: BaseTable<E>, val database: Database) : IRepository<E, D> {
  override fun findById(id: UUID): D? {
    // TODO: make a little bit less hacky wacky
    val resultSet = database.from(table)
      .select()
      .where { table.id eq id }
    var t: D? = null
    resultSet.forEach { row -> t = convertToClass(row) }
    return t
  }

  override fun findAll(page: Int, pageSize: Int): Page<D> {
    val resultSet = database.from(table).select().limit(page, pageSize)
    return Page(
      page,
      pageSize,
      1,
      currentList = resultSet.map { row -> convertToClass(row) }
    )
  }

  override fun delete(id: UUID): Int {
    // TODO: remember to test that this actually works
    return database.delete(table) { it.id eq id }
  }
}

package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.*
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.model.BaseModel
import no.fg.hilflingbackend.model.BaseTable
import java.util.*

abstract class BaseRepository<T: BaseModel<T>>(val table: BaseTable<T>, val database: Database): IRepository<T> {
  override fun findById(id: UUID): T? {
    // TODO: make a little bit less hacky wacky
    val resultSet = database.from(table)
      .select()
      .where { table.id eq id }
    var t: T? = null
    resultSet.forEach { row -> t = convertToClass(row) }
    return t
  }

  override fun findAll(offset: Int, limit: Int): Page<T> {
    val resultSet = database.from(table).select().limit(offset, limit)
    return Page(
      offset,
      limit,
      1,
      currentList = resultSet.map { row -> convertToClass(row) }
    )
  }

  override fun delete(id: UUID): Int {
    // TODO: remember to test that this actually works
    return database.delete(table){it.id eq id}
  }
}

package no.fg.hilflingbackend.repository

import javassist.NotFoundException
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.forEach
import me.liuwj.ktorm.dsl.from
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.entity.*
import me.liuwj.ktorm.schema.BaseTable
import me.liuwj.ktorm.schema.Table
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.model.BaseModel
import no.fg.hilflingbackend.model.Categories
import no.fg.hilflingbackend.model.Category
import no.fg.hilflingbackend.model.categories
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.util.*


open class BaseRepository<T: BaseModel<T>>(val table: BaseTable<T>, val database: Database): IRepository<T> {
  override fun findById(id: UUID): T? {
    TODO("Not yet implemented")
  }

  override fun create(entity: T): T {
    TODO("Not yet implemented")
  }

  override fun findAll(offset: Int, limit: Int): Page<T> {
    val resultSet = database.from(table).select()
    val columnCount = resultSet.rowSet.metaData.columnCount
    println(columnCount.toString())
    val entityList = listOf<T>()
    resultSet.forEach { row ->
      run {
        for (column in table.columns) {
          println(row[column])
        }
      }
    }
    return Page(
      offset,
      limit,
      1,
      currentList = listOf()
    )
  }

  override fun delete(id: UUID): Int {
    TODO("Not yet implemented")
  }

  override fun patch(entity: T): Int {
    TODO("Not yet implemented")
  }
}

@Repository
open class CategoryRepository(database: Database) : BaseRepository<Category>(table = Categories, database) {

}

/*
@Repository
open class CategoryRepository (val database: Database): IRepository<Category>{

  override fun findById(id: UUID): Category? {
    return database.categories.find { it.id eq id }
  }

  override fun findAll(offset: Int, limit: Int): Page<Category> {
    val categories = database.categories.drop(offset).take(limit)
    return Page(
      offset,
      limit,
      categories.totalRecords,
      currentList = categories.toList()
    )
  }

  override fun create(
    entity: Category
  ): Category {
    val categoryFromDatabase = Category {
      this.name = entity.name
    }
    database.categories.add(categoryFromDatabase)
    return categoryFromDatabase
  }

  override fun delete(id: UUID): Int {
    val category = database.categories.find { it.id eq id }
      ?: throw NotFoundException("Category not found")
    return category.delete()
  }

  override fun patch(entity: Category): Int {
    return database.categories.update(entity)
  }
}
*/

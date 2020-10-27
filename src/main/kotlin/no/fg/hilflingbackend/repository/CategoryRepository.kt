package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.*
import no.fg.hilflingbackend.model.*
import org.springframework.stereotype.Repository

@Repository
open class CategoryRepository(database: Database) : BaseRepository<Category>(table = Categories, database) {
  override fun convertToClass(qrs: QueryRowSet): Category {
    return Category{
      id = qrs[Categories.id]!!
      dateCreated = qrs[Categories.dateCreated]!!
      name = qrs[Categories.name]!!
    }
  }

  override fun create(entity: Category): Int {
    return database.categories.add(entity)
  }

  override fun patch(entity: Category): Int {
    return database.categories.update(entity)
  }
}

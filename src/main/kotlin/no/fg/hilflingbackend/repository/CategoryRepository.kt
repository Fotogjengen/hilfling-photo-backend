package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.*
import no.fg.hilflingbackend.dto.*
import no.fg.hilflingbackend.model.*
import org.springframework.stereotype.Repository

@Repository
open class CategoryRepository(database: Database) : BaseRepository<Category, CategoryDto>(table = Categories, database) {
  override fun convertToClass(qrs: QueryRowSet): CategoryDto = CategoryDto(
    categoryId = CategoryId(qrs[Categories.id]!!),
    name = qrs[Categories.name]!!
  )

  override fun create(dto: CategoryDto): Int {
    return database.categories.add(dto.toEntity())
  }

  override fun patch(dto: CategoryDto): Int {
    return database.categories.update(dto.toEntity())
  }
}

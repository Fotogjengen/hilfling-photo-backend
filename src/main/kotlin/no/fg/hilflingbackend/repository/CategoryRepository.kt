package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.dto.CategoryDto
import no.fg.hilflingbackend.dto.CategoryId
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.Categories
import no.fg.hilflingbackend.model.Category
import no.fg.hilflingbackend.model.categories
import no.fg.hilflingbackend.model.toDto
import org.springframework.stereotype.Repository

@Repository
open class CategoryRepository(database: Database) : BaseRepository<Category, CategoryDto>(table = Categories, database = database) {
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

  fun findByName(categoryName: String) = database
    .categories
    .find {
      it.name eq categoryName
    }
    ?.toDto()
}

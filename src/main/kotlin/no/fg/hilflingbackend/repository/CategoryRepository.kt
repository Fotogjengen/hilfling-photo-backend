package no.fg.hilflingbackend.repository

import jakarta.persistence.EntityNotFoundException
import no.fg.hilflingbackend.dto.CategoryDto
import no.fg.hilflingbackend.dto.CategoryId
import no.fg.hilflingbackend.dto.CategoryPatchRequestDto
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.Categories
import no.fg.hilflingbackend.model.Category
import no.fg.hilflingbackend.model.categories
import no.fg.hilflingbackend.model.toDto
import org.ktorm.database.Database
import org.ktorm.dsl.QueryRowSet
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.ktorm.entity.update
import org.springframework.stereotype.Repository

@Repository
open class CategoryRepository(database: Database) : BaseRepository<Category, CategoryDto, CategoryPatchRequestDto>(table = Categories, database = database) {
  override fun convertToClass(qrs: QueryRowSet): CategoryDto = CategoryDto(
    categoryId = CategoryId(qrs[Categories.id]!!),
    name = qrs[Categories.name]!!
  )

  override fun create(dto: CategoryDto): Int {
    return database.categories.add(dto.toEntity())
  }

  override fun patch(dto: CategoryPatchRequestDto): CategoryDto {
    val fromDb = findById(dto.categoryId.id)
      ?: throw EntityNotFoundException("Could not find Category")
    val newDto = CategoryDto(
      categoryId = fromDb.categoryId,
      name = dto.name ?: fromDb.name
    )
    val updated = database.categories.update(newDto.toEntity())

    return if (updated == 1) newDto else fromDb
  }

  fun findByName(categoryName: String) = database
    .categories
    .find {
      it.name eq categoryName
    }
    ?.toDto()
}

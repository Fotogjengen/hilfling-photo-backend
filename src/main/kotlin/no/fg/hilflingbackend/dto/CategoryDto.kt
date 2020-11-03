package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.Category
import java.util.UUID

data class CategoryDto(
  val categoryId: CategoryId = CategoryId(),
  val name: String,
)

data class CategoryId(
  override val id: UUID = UUID.randomUUID()
) : UuidId {
  override fun toString(): String = id.toString()
}

fun CategoryDto.toEntity(): Category {
  val dto = this
  return Category {
    id = dto.categoryId.id
    name = dto.name
  }
}

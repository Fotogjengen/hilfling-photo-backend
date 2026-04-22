package no.fg.hilflingbackend.model

import no.fg.hilflingbackend.dto.CategoryDto
import no.fg.hilflingbackend.dto.CategoryId
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.varchar

interface Category : BaseModel<Category> {
  companion object : Entity.Factory<Category>()

  var name: String
}

fun Category.toDto(): CategoryDto = CategoryDto(
  categoryId = CategoryId(this.id),
  name = this.name
)

object Categories : BaseTable<Category>("category") {
  val name = varchar("name").bindTo { it.name }
}

val Database.categories get() = this.sequenceOf(Categories)

package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.varchar
import no.fg.hilflingbackend.dto.CategoryDto
import no.fg.hilflingbackend.dto.CategoryId

interface Category : BaseModel<Category> {
  companion object : Entity.Factory<Category>()

  var name: String
}

fun Category.toDto() = CategoryDto(
  categoryId = CategoryId(),
  name = this.name
)

object Categories : BaseTable<Category>("category") {
  val name = varchar("name").bindTo { it.name }
}

val Database.categories get() = this.sequenceOf(Categories)

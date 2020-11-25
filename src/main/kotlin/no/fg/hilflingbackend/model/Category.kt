package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.varchar

interface Category : BaseModel<Category> {
  companion object : Entity.Factory<Category>()

  var name: String?
}

object Categories : BaseTable<Category>("category") {
  val name = varchar("name").bindTo { it.name }
}

val Database.categories get() = this.sequenceOf(Categories)

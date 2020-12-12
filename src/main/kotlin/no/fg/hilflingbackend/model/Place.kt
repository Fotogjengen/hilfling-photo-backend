package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.varchar

interface Place : BaseModel<Place> {
  companion object : Entity.Factory<Place>()

  var name: String?
}

object Places : BaseTable<Place>("place") {
  val name = varchar("name").bindTo { it.name }
}

val Database.places get() = this.sequenceOf(Places)

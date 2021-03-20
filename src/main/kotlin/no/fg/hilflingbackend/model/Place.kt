package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.varchar
import no.fg.hilflingbackend.dto.PlaceDto
import no.fg.hilflingbackend.dto.PlaceId

interface Place : BaseModel<Place> {
  companion object : Entity.Factory<Place>()

  var name: String
}

fun Place.toDto(): PlaceDto = PlaceDto(
  placeId = PlaceId(this.id),
  name = this.name
)

object Places : BaseTable<Place>("place") {
  val name = varchar("name").bindTo { it.name }
}

val Database.places get() = this.sequenceOf(Places)

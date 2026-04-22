package no.fg.hilflingbackend.model

import no.fg.hilflingbackend.dto.PlaceDto
import no.fg.hilflingbackend.dto.PlaceId
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.varchar

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

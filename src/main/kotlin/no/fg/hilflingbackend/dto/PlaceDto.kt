package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.Place
import java.util.UUID

data class PlaceDto(
  val placeId: PlaceId = PlaceId(),
  val name: String
)

data class PlaceId(
  override val id: UUID = UUID.randomUUID()
) : UuidId {
  override fun toString(): String = id.toString()
}

fun PlaceDto.toEntity(): Place {
  val dto = this
  return Place {
    id = dto.placeId.id
    name = dto.name
  }
}

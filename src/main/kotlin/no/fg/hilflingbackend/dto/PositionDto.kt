package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.Position
import no.fg.hilflingbackend.value_object.Email
import java.util.UUID

data class PositionDto(
  val positionId: PositionId = PositionId(),
  val title: String,
  val email: Email
)

fun PositionDto.toEntity(): Position {
  val dto = this
  return Position {
    id = dto.positionId.id
    title = dto.title
    email = dto.email.value
  }
}

data class PositionId(
  override val id: UUID = UUID.randomUUID()
) : UuidId {
  override fun toString(): String = id.toString()
}

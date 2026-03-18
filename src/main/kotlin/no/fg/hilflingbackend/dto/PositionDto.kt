package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.Position
import java.util.UUID

data class PositionPatchRequestDto(
  val positionId: PositionId,
  val title: String?,
  val email: String?,
)

data class PositionDto(
  val positionId: PositionId = PositionId(),
  val title: String,
  val email: String,
)

fun PositionDto.toEntity(): Position {
  val dto = this
  return Position {
    id = dto.positionId.id
    title = dto.title
    email = dto.email
  }
}

data class PositionId(
  override val id: UUID = UUID.randomUUID(),
) : UuidId {
  override fun toString(): String = id.toString()
}

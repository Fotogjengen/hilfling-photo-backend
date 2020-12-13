package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.PhotoGangBangerPosition
import java.util.UUID

data class PhotoGangBangerPositionDto(
  val photoGangBangerPositionId: PhotoGangBangerPositionId = PhotoGangBangerPositionId(),
  val photoGangBangerDto: PhotoGangBangerDto,
  val position: PositionDto,
  val isCurrent: Boolean,
)

fun PhotoGangBangerPositionDto.toEntity(): PhotoGangBangerPosition {
  val dto = this
  return PhotoGangBangerPosition {
    id = dto.photoGangBangerPositionId.id
    position = dto.position.toEntity()
    isCurrent = dto.isCurrent
  }
}

data class PhotoGangBangerPositionId(
  override val id: UUID = UUID.randomUUID()
) : UuidId {
  override fun toString(): String = id.toString()
}

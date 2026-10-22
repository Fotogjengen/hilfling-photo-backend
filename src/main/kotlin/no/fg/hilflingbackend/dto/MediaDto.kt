package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.Media
import java.util.UUID

data class MediaPatchRequestDto(
  val mediaId: MediaId,
  val medium: String?,
  val analog: Boolean?,
)

data class MediaDto(
  val mediaId: MediaId = MediaId(),
  val medium: String,
  val analog: Boolean,
)

data class MediaId(
  override val id: UUID = UUID.randomUUID(),
) : UuidId {
  override fun toString(): String = id.toString()
}

fun MediaDto.toEntity(): Media {
  val dto = this
  return Media {
    id = dto.mediaId.id
    medium = dto.medium
    analog = dto.analog
  }
}

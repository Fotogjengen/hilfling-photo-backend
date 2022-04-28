package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.PhotoTag
import java.util.UUID

data class PhotoTagPatchRequestDto(
  val photoTagId: PhotoTagId,
  val name: String?
)

data class PhotoTagDto(
  val photoTagId: PhotoTagId = PhotoTagId(),
  val name: String
) {
  override fun equals(other: Any?): Boolean {
    return other is PhotoTagDto && this.photoTagId.id == other.photoTagId.id
  }
}

data class PhotoTagId(
  override val id: UUID = UUID.randomUUID()
) : UuidId {
  override fun toString(): String = id.toString()
}

fun PhotoTagDto.toEntity(): PhotoTag {
  val dto = this
  return PhotoTag {
    id = dto.photoTagId.id
    name = dto.name
  }
}

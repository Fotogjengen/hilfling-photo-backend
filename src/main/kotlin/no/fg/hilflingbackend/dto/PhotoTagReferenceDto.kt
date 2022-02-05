package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.Photo
import no.fg.hilflingbackend.model.PhotoTag
import no.fg.hilflingbackend.model.PhotoTagReference
import java.util.UUID

data class PhotoTagReferenceDto(
  val photoTagReferenceId: PhotoTagReferenceId = PhotoTagReferenceId(),
  val photoTag: PhotoTag,
  val photo: Photo
)

data class PhotoTagReferenceId(
  override val id: UUID = UUID.randomUUID()
) : UuidId {
  override fun toString(): String = id.toString()
}

fun PhotoTagReferenceDto.toEntity(): PhotoTagReference {
  val dto = this
  return PhotoTagReference {
    id = dto.photoTagReferenceId.id
    photo = dto.photo
    photoTag = dto.photoTag
  }
}

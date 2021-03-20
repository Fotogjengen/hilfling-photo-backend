package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.PhotoTag
import java.util.UUID

data class PhotoTagDto(

  val photoTagId: PhotoTagId = PhotoTagId(),
  val name: String
)

data class PhotoTagId(
  override val id: UUID = UUID.randomUUID()
) : UuidId {
  override fun toString(): String = id.toString()
}

fun PhotoTagDto.toEntity(): PhotoTag = PhotoTag {
  id = id
  name = name
}

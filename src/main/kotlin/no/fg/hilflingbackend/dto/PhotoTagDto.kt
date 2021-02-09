package no.fg.hilflingbackend.dto

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

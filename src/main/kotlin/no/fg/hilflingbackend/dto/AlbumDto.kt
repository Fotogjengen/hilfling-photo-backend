package no.fg.hilflingbackend.dto

import java.util.UUID

data class AlbumDto(
  val albumId: AlbumId = AlbumId(),
  val title: String,
  val isAnalog: Boolean
)

data class AlbumId (
    override val id: UUID = UUID.randomUUID()
) : UuidId {
  override fun toString(): String = id.toString()
}

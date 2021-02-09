package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.Album
import java.util.UUID

data class AlbumDto(
  val albumId: AlbumId = AlbumId(),
  val title: String,
  val isAnalog: Boolean = false
)

data class AlbumId(
  override val id: UUID = UUID.randomUUID()
) : UuidId {
  override fun toString(): String = id.toString()
}

fun AlbumDto.toEntity(): Album {
  val dto = this
  return Album {
    id = dto.albumId.id
    title = dto.title
    isAnalog = dto.isAnalog
  }
}

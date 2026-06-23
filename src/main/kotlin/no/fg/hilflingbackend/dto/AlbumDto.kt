package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.Album
import java.util.UUID

data class AlbumPatchRequestDto(
  val albumId: AlbumId,
  val name: String,
  val description: String?,
  val analog: Boolean?,
)

data class AlbumDto(
  val albumId: AlbumId = AlbumId(),
  val name: String,
  val description: String? = null,
  val analog: Boolean = false,
)

data class AlbumId(
  override val id: UUID = UUID.randomUUID(),
) : UuidId {
  override fun toString(): String = id.toString()
}

fun AlbumDto.toEntity(): Album {
  val dto = this
  return Album {
    id = dto.albumId.id
    name = dto.name
    description = dto.description
    analog = dto.analog
  }
}

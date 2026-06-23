package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.Album
import no.fg.hilflingbackend.model.PhotoReservation
import java.time.LocalDateTime

data class PhotoReservationDto(
  val albumId: AlbumId,
  val pageNumber: Int,
  val imageNumber: Int,
  val reservedAt: LocalDateTime,
  val album: AlbumDto? = null,
)

fun PhotoReservationDto.toEntity(): PhotoReservation {
  val dto = this
  return PhotoReservation {
    this.album = Album { id = dto.albumId.id }
    this.pageNumber = dto.pageNumber
    this.imageNumber = dto.imageNumber
    this.reservedAt = dto.reservedAt
  }
}

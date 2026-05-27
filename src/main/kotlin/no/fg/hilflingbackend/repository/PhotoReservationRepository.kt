package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.delete
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.dto.PhotoReservationDto
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.PhotoReservations
import no.fg.hilflingbackend.model.photo_reservations
import no.fg.hilflingbackend.model.toDto
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
open class PhotoReservationRepository(
  val database: Database,
) {
  fun createReservation(dto: PhotoReservationDto): PhotoReservationDto {
    database.photo_reservations.add(dto.toEntity())
    return dto
  }

  fun findLastReservation(albumId: UUID): PhotoReservationDto? =
    database.photo_reservations
      .filter { it.albumId eq albumId }
      .toList()
      .maxByOrNull { it.pageNumber * 100 + it.imageNumber }
      ?.toDto()

  fun findByAlbum(albumId: UUID): List<PhotoReservationDto> =
    database.photo_reservations
      .filter { it.albumId eq albumId }
      .toList()
      .map { it.toDto() }

  fun findReservation(
    albumId: UUID,
    pageNumber: Int,
    imageNumber: Int,
  ): PhotoReservationDto? =
    database.photo_reservations
      .find { (it.albumId eq albumId) and (it.pageNumber eq pageNumber) and (it.imageNumber eq imageNumber) }
      ?.toDto()

  fun deleteReservation(
    albumId: UUID,
    pageNumber: Int,
    imageNumber: Int,
  ) {
    database.delete(PhotoReservations) {
      (it.albumId eq albumId) and (it.pageNumber eq pageNumber) and (it.imageNumber eq imageNumber)
    }
  }
}

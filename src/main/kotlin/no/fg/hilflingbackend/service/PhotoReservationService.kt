package no.fg.hilflingbackend.service

import no.fg.hilflingbackend.dto.AlbumId
import no.fg.hilflingbackend.dto.PhotoReservationDto
import no.fg.hilflingbackend.repository.PhotoRepository
import no.fg.hilflingbackend.repository.PhotoReservationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

/**
 * Service for handling photo reservations.
 *
 * Albums are organized into pages and images, both numbered 1–99.
 * When imageNumber reaches 99 it wraps to imageNumber 1 on the next page.
 * An album is considered full when pageNumber also reaches 99.
 */
@Service
class PhotoReservationService(
  val photoRepository: PhotoRepository,
  val photoReservationRepository: PhotoReservationRepository,
) {
  /**
   * Creates the next available reservation in the given album.
   *
   * Slot ordering: imageNumber increments first; when it exceeds 99 it resets to 1
   * and pageNumber increments. The last occupied slot is derived from whichever is
   * higher between existing photos and active reservations, since reservations are
   * deleted when a photo is inserted. Throws if the album is already at page 99, image 99.
   *
   * @param albumId the album to reserve a slot in
   * @return the newly created reservation
   * @throws IllegalStateException if the album is full
   */
  @Transactional(isolation = Isolation.SERIALIZABLE)
  fun createReservation(albumId: UUID): PhotoReservationDto {
    val lastReservation =
      photoReservationRepository
        .findLastReservation(albumId)
        ?.let { it.pageNumber to it.imageNumber }
    val lastPhoto = photoRepository.findLastByAlbum(albumId)

    val last =
      listOfNotNull(lastReservation, lastPhoto)
        .maxByOrNull { (page, image) -> page * 100 + image }

    val (nextPage, nextImage) =
      when {
        last == null -> {
          1 to 1
        }

        last.second >= 99 -> {
          if (last.first >= 99) error("Album $albumId is full")
          last.first + 1 to 1
        }

        else -> {
          last.first to last.second + 1
        }
      }

    val reservation =
      PhotoReservationDto(
        albumId = AlbumId(albumId),
        pageNumber = nextPage,
        imageNumber = nextImage,
        reservedAt = LocalDateTime.now(),
      )
    return photoReservationRepository.createReservation(reservation)
  }

  fun findReservation(
    albumId: UUID,
    pageNumber: Int,
    imageNumber: Int,
  ): PhotoReservationDto? = photoReservationRepository.findReservation(albumId, pageNumber, imageNumber)

  /**
   * Deletes a reservation.
   *
   * IMPORTANT: This should only ever be called after a photo insertion happened. We do not want photo conflicts.
   *
   * @param albumId the album the reservation belongs to.
   * @param pageNumber the page number of the reservation.
   * @param imageNumber the image number of the reservation.
   */
  fun deleteReservation(
    albumId: UUID,
    pageNumber: Int,
    imageNumber: Int,
  ) {
    photoReservationRepository.deleteReservation(albumId, pageNumber, imageNumber)
  }
}

package no.fg.hilflingbackend.service

import jakarta.persistence.EntityNotFoundException
import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.dto.PhotoReservationDto
import no.fg.hilflingbackend.dto.PhotoUploadRequestDto
import no.fg.hilflingbackend.repository.AlbumRepository
import no.fg.hilflingbackend.repository.CategoryRepository
import no.fg.hilflingbackend.repository.EventOwnerRepository
import no.fg.hilflingbackend.repository.PhotoGangBangerRepository
import no.fg.hilflingbackend.repository.PhotoRepository
import no.fg.hilflingbackend.repository.PlaceRepository
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class PhotoService(
  val photoRepository: PhotoRepository,
  val eventOwnerRepository: EventOwnerRepository,
  val placeRepository: PlaceRepository,
  val categoryRepository: CategoryRepository,
  val albumRepository: AlbumRepository,
  val photoGangBangerRepository: PhotoGangBangerRepository,
  val photoReservationService: PhotoReservationService,
) {
  /**
   * Retrieves a photo by ID if the user's security level grants access.
   *
   * @param photoId the ID of the photo to retrieve
   * @param userSecurityLevel the security level of the requesting user
   * @return the photo
   * @throws EntityNotFoundException if the photo does not exist
   * @throws AccessDeniedException if the user's security level is insufficient
   */
  fun findById(photoId: UUID, userSecurityLevel: SecurityLevelType): PhotoDto {
    val photo = photoRepository.findById(photoId, userSecurityLevel)
      ?: throw EntityNotFoundException("Photo $photoId not found")
    if (photo.securityLevel.securityLevelType.ordinal < userSecurityLevel.ordinal) {
      throw AccessDeniedException("Insufficient security level to access photo $photoId")
    }
    return photo
  }

  /**
   * Commits a photo to the database and deletes the corresponding reservation.
   *
   * The reservation must exist for the given album, page, and image number.
   * The photo creation and reservation deletion are performed atomically.
   *
   * Note: This method assumes that the incomming PhotoDto has not been modifies since the 
   * reservation was made. For our system, this does not matter since the proxy does not mutate it. 
   * HOWEVER, if we in the future (somehow) change this, we need to re-validate here
   *
   * @param photo the complete photo data including URLs and slot numbers
   * @return the created photo
   * @throws EntityNotFoundException if no reservation exists for the given slot
   */
  @Transactional
  fun upload(photo: PhotoDto): PhotoDto {
    val album =
      (if (photo.analog) photo.motive.analogAlbumDto else photo.motive.albumDto)
        ?: throw EntityNotFoundException("Motive ${photo.motive.motiveId} has no album")

    photoReservationService.findReservation(album.albumId.id, photo.pageNumber, photo.imageNumber)
      ?: throw EntityNotFoundException("No reservation found for slot (page=${photo.pageNumber}, image=${photo.imageNumber}) in album ${album.albumId}")

    photoRepository.createPhoto(photo)
    photoReservationService.deleteReservation(album.albumId.id, photo.pageNumber, photo.imageNumber)
    return photo
  }

  /**
   * Soft-deletes a photo.
   *
   * @param photoId the ID of the photo to delete
   * @throws EntityNotFoundException if no photo exists with the given ID
   */
  fun findByMotiveId(motiveId: UUID, userSecurityLevel: SecurityLevelType): List<PhotoDto> =
    photoRepository.findByMotiveId(motiveId, userSecurityLevel)
      .filter { it.securityLevel.securityLevelType.ordinal >= userSecurityLevel.ordinal }

  fun delete(photoId: UUID) {
    photoRepository.findById(photoId, SecurityLevelType.FG) 
      ?: throw EntityNotFoundException("Photo $photoId not found")
    photoRepository.deletePhoto(photoId)
  }

  /**
   * Validates the upload request and reserves the next available slot in the album.
   *
   * @param request the upload request containing photo metadata
   * @return the reserved slot
   * @throws IllegalArgumentException if validation fails or the motive has no album
   */
  fun reserve(request: PhotoUploadRequestDto): PhotoReservationDto {
    val errors = validate(request)
    if (errors.isNotEmpty()) throw IllegalArgumentException(errors.joinToString(", "))
    return reserveSlot(request)
  }

  /**
   * Reserves a imagenumber/pagenumber slot for a pgoto
   */
  private fun reserveSlot(request: PhotoUploadRequestDto): PhotoReservationDto {
    val album =
      if (request.analog) {
        request.motive.analogAlbumDto
          ?: throw IllegalArgumentException("Motive ${request.motive.motiveId} has no analog album")
      } else {
        request.motive.albumDto
          ?: throw IllegalArgumentException("Motive ${request.motive.motiveId} has no digital album")
      }
    return photoReservationService.createReservation(album.albumId.id)
  }

  /**
   * Validates a photo upload request
   */
  private fun validate(request: PhotoUploadRequestDto): List<String> {
    val albumTitle = if (request.analog) request.motive.analogAlbumDto?.name else request.motive.albumDto?.name
    val photoGangBangerId = request.photoGangBangerDto.photoGangBangerId.id
    val placeId = request.motive.placeDto.name
    val categoryId = request.motive.categoryDto.name
    val eventOwnerId = request.motive.eventOwnerDto.name
    val errors = mutableListOf<String>()

    if (placeRepository.findByName(placeId) == null) {
      errors.add("Place with name $placeId does not exist")
    }
    if (categoryRepository.findByName(categoryId) == null) {
      errors.add("Category with name $categoryId does not exist")
    }
    if (eventOwnerRepository.findByEventOwnerName(eventOwnerId) == null) {
      errors.add("EventOwner with name $eventOwnerId does not exist")
    }
    if (photoGangBangerRepository.findById(photoGangBangerId) == null) {
      errors.add("PhotoGangBanger with id $photoGangBangerId does not exist")
    }

    // Validate that the album exists and matches the photo type (analog/digital)
    if (albumTitle == null) errors.add("Motive has no ${if (request.analog) "analog" else "digital"} album")

    if (albumTitle != null) {
      val album = albumRepository.findByName(albumTitle)
      if (album == null) {
        errors.add("Album with title $albumTitle does not exist")
      } else if (album.analog != request.analog) {
        val expected = if (request.analog) "analog" else "digital"
        val actual = if (album.analog) "analog" else "digital"
        errors.add("Cannot upload a ${expected} photo into a ${actual} album")
      }
    }

    return errors.toList()
  }
}

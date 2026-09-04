package no.fg.hilflingbackend.service

import jakarta.persistence.EntityNotFoundException
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.dto.PhotoFinalizeRequestDto
import no.fg.hilflingbackend.dto.PhotoId
import no.fg.hilflingbackend.dto.PhotoPositionDto
import no.fg.hilflingbackend.dto.PhotoReservationDto
import no.fg.hilflingbackend.dto.PhotoUploadRequestDto
import no.fg.hilflingbackend.repository.GangRepository
import no.fg.hilflingbackend.repository.MotiveRepository
import no.fg.hilflingbackend.repository.PhotoGangBangerRepository
import no.fg.hilflingbackend.repository.PhotoRepository
import no.fg.hilflingbackend.valueobject.Permission
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.UUID

@Service
class PhotoService(
  val photoRepository: PhotoRepository,
  val motiveRepository: MotiveRepository,
  val gangRepository: GangRepository,
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
  fun findById(
    photoId: UUID,
    userSecurityLevel: SecurityLevelType,
  ): PhotoDto {
    val photo =
      photoRepository.findById(photoId, userSecurityLevel)
        ?: throw EntityNotFoundException("Photo $photoId not found")
    if (photo.securityLevel.securityLevelType.ordinal < userSecurityLevel.ordinal) {
      throw AccessDeniedException("Insufficient security level to access photo $photoId")
    }
    return photo
  }

  @Transactional
  fun upload(
    request: PhotoFinalizeRequestDto,
    username: String,
  ): PhotoDto {
    val motive =
      motiveRepository.findById(request.motiveId)
        ?: throw EntityNotFoundException("Motive ${request.motiveId} not found")
    val album =
      (if (request.analog) motive.analogAlbumDto else motive.albumDto)
        ?: throw EntityNotFoundException("Motive ${request.motiveId} has no ${if (request.analog) "analog" else "digital"} album")

    photoReservationService.findReservation(album.albumId.id, request.pageNumber, request.imageNumber)
      ?: throw EntityNotFoundException("No reservation found for slot (page=${request.pageNumber}, image=${request.imageNumber}) in album ${album.albumId}")

    val photoGangBanger =
      photoGangBangerRepository.findByUsername(username)
        ?: throw EntityNotFoundException("PhotoGangBanger for user '$username' not found")
    val gang =
      request.gangId?.let {
        gangRepository.findById(it) ?: throw EntityNotFoundException("Gang $it not found")
      }

    val photo =
      PhotoDto(
        photoId = PhotoId(),
        goodPicture = request.goodPicture,
        analog = request.analog,
        imageNumber = request.imageNumber,
        pageNumber = request.pageNumber,
        imageProd = request.imageProd,
        imageWeb = request.imageWeb,
        imageThumb = request.imageThumb,
        motive = motive,
        gang = gang,
        photoGangBangerDto = photoGangBanger,
        dateUploaded = request.dateTaken,
      )
    photoRepository.createPhoto(photo)
    photoReservationService.deleteReservation(album.albumId.id, request.pageNumber, request.imageNumber)
    return photo
  }

  /**
   * Retrieves the photos belonging to a motive that the user is allowed to see.
   *
   * @param motiveId the ID of the motive whose photos to retrieve
   * @param userSecurityLevel the security level of the requesting user
   * @return the photos of the motive
   * @throws EntityNotFoundException if the motive does not exist or is not visible at the user's security level
   */
  fun findByMotiveId(
    motiveId: UUID,
    userSecurityLevel: SecurityLevelType,
  ): List<PhotoDto> {
    val motive =
      motiveRepository.findById(motiveId)
        ?: throw EntityNotFoundException("Motive $motiveId not found")
    if (motive.securityLevel.securityLevelType.ordinal < userSecurityLevel.ordinal) {
      throw EntityNotFoundException("Motive $motiveId not found")
    }
    return photoRepository
      .findByMotiveId(motiveId, userSecurityLevel)
      .filter { it.securityLevel.securityLevelType.ordinal >= userSecurityLevel.ordinal }
  }

  fun delete(
    photoId: UUID,
    userSecurityLevel: SecurityLevelType,
    userPermissions: List<Permission>,
  ): PhotoDto {
    if (userSecurityLevel != SecurityLevelType.FG) {
      throw AccessDeniedException("Only users with FG security level can delete photos")
    }

    val photo =
      photoRepository.findById(photoId, userSecurityLevel)
        ?: throw EntityNotFoundException("Photo $photoId not found")

    val olderThanThirtyDays = photo.dateUploaded.isBefore(LocalDate.now().minusDays(30))
    if (olderThanThirtyDays && Permission.PHOTO_DELETE_OLD !in userPermissions) {
      throw AccessDeniedException("Deleting photos older than 30 days requires the PHOTO_DELETE_OLD permission")
    }

    photoRepository.deletePhoto(photoId)
    return photo
  }

  /**
   * Validates the upload request and reserves the next available slot in the album.
   *
   * @param request the upload request containing photo metadata
   * @return the reserved slot
   * @throws IllegalArgumentException if validation fails or the motive has no album
   */
  fun reserve(
    request: PhotoUploadRequestDto,
    username: String,
  ): PhotoReservationDto {
    val errors = validate(request, username)
    if (errors.isNotEmpty()) throw IllegalArgumentException(errors.joinToString(", "))
    return reserveSlot(request)
  }

  fun findGoodPicturesPage(
    page: Int,
    pageSize: Int,
    userSecurityLevel: SecurityLevelType,
  ): Page<PhotoDto> = photoRepository.findGoodPicturesPage(page, pageSize, userSecurityLevel)

  fun findGoodPicturePosition(
    photoId: UUID,
    pageSize: Int,
    userSecurityLevel: SecurityLevelType,
  ): PhotoPositionDto =
    photoRepository.findGoodPicturePosition(photoId, pageSize, userSecurityLevel)
      ?: throw EntityNotFoundException("Photo $photoId is not in the good pictures listing")

  fun findGoodPicturesByMotiveId(
    motiveId: UUID,
    userSecurityLevel: SecurityLevelType,
  ): List<PhotoDto> {
    val motive =
      motiveRepository.findById(motiveId)
        ?: throw EntityNotFoundException("Motive $motiveId not found")
    if (motive.securityLevel.securityLevelType.ordinal < userSecurityLevel.ordinal) {
      throw EntityNotFoundException("Motive $motiveId not found")
    }
    return photoRepository
      .findGoodPicturesByMotiveId(motiveId, userSecurityLevel)
      .filter { it.securityLevel.securityLevelType.ordinal >= userSecurityLevel.ordinal }
  }

  fun markAsGoodPicture(
    photoId: UUID,
    goodPicture: Boolean,
    userSecurityLevel: SecurityLevelType,
  ) {
    val photo =
      photoRepository.findById(photoId, userSecurityLevel)
        ?: throw EntityNotFoundException("Photo $photoId not found")
    if (photo.securityLevel.securityLevelType.ordinal < userSecurityLevel.ordinal) {
      throw AccessDeniedException("Insufficient security level to modify photo $photoId")
    }
    photoRepository.updateGoodPicture(photoId, goodPicture)
  }

  private fun reserveSlot(request: PhotoUploadRequestDto): PhotoReservationDto {
    val motive =
      motiveRepository.findById(request.motiveId)
        ?: throw EntityNotFoundException("Motive ${request.motiveId} not found")
    val album =
      (if (request.analog) motive.analogAlbumDto else motive.albumDto)
        ?: throw IllegalArgumentException("Motive ${request.motiveId} has no ${if (request.analog) "analog" else "digital"} album")
    return photoReservationService.createReservation(album.albumId.id).copy(album = album)
  }

  private fun validate(
    request: PhotoUploadRequestDto,
    username: String,
  ): List<String> {
    val errors = mutableListOf<String>()
    val motive = motiveRepository.findById(request.motiveId)
    if (motive == null) {
      errors.add("Motive with id ${request.motiveId} does not exist")
    } else {
      val album = if (request.analog) motive.analogAlbumDto else motive.albumDto
      if (album == null) errors.add("Motive has no ${if (request.analog) "analog" else "digital"} album")
    }
    if (photoGangBangerRepository.findByUsername(username) == null) {
      errors.add("No PhotoGangBanger found for user '$username'")
    }
    return errors.toList()
  }
}

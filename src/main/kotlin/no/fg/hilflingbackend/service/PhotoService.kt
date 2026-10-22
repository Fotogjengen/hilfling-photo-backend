package no.fg.hilflingbackend.service

import jakarta.persistence.EntityNotFoundException
import no.fg.hilflingbackend.dto.AlbumDto
import no.fg.hilflingbackend.dto.MediaDto
import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.dto.PhotoFinalizeRequestDto
import no.fg.hilflingbackend.dto.PhotoId
import no.fg.hilflingbackend.dto.PhotoMoveRequestDto
import no.fg.hilflingbackend.dto.PhotoPatchRequestDto
import no.fg.hilflingbackend.dto.PhotoPositionDto
import no.fg.hilflingbackend.dto.PhotoReservationDto
import no.fg.hilflingbackend.dto.PhotoUploadRequestDto
import no.fg.hilflingbackend.dto.albumFor
import no.fg.hilflingbackend.repository.GangRepository
import no.fg.hilflingbackend.repository.MediaRepository
import no.fg.hilflingbackend.repository.MotiveRepository
import no.fg.hilflingbackend.repository.PhotoGangBangerRepository
import no.fg.hilflingbackend.repository.PhotoRepository
import no.fg.hilflingbackend.valueobject.Permission
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import java.time.LocalDate
import java.util.UUID

@Service
class PhotoService(
  val photoRepository: PhotoRepository,
  val motiveRepository: MotiveRepository,
  val mediaRepository: MediaRepository,
  val gangRepository: GangRepository,
  val photoGangBangerRepository: PhotoGangBangerRepository,
  val photoReservationService: PhotoReservationService,
  val motiveLocks: MotiveLocks,
  val transactionTemplate: TransactionTemplate,
  val photoProviderClient: PhotoProviderClient,
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

  // Commits inside the motive lock so a concurrent relocation sees the new photo
  fun upload(
    request: PhotoFinalizeRequestDto,
    username: String,
  ): PhotoDto =
    motiveLocks.withLock(request.motiveId) {
      transactionTemplate.execute { uploadLocked(request, username) }!!
    }

  private fun uploadLocked(
    request: PhotoFinalizeRequestDto,
    username: String,
  ): PhotoDto {
    val motive =
      motiveRepository.findById(request.motiveId)
        ?: throw EntityNotFoundException("Motive ${request.motiveId} not found")
    val media =
      mediaRepository.findOrNull(request.mediaId)
        ?: throw EntityNotFoundException("Media ${request.mediaId} not found")
    val album =
      motive.albumFor(media)
        ?: throw EntityNotFoundException("Motive ${request.motiveId} has no ${kind(media)} album")
    requireCurrentSecurityLevel(request.securityLevel, motive)

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
        imageNumber = request.imageNumber,
        pageNumber = request.pageNumber,
        imageProd = request.imageProd,
        imageWeb = request.imageWeb,
        imageThumb = request.imageThumb,
        motive = motive,
        media = media,
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

    // The soft delete rolls back if the provider fails to unlink the files
    return motiveLocks.withLock(photo.motive.motiveId.id) {
      transactionTemplate.execute {
        photoRepository.deletePhoto(photoId)
        photoProviderClient.delete(
          listOf(
            PhotoFiles(
              photoId = photoId.toString(),
              current = ImageUrls(prod = photo.imageProd, web = photo.imageWeb, thumb = photo.imageThumb),
            ),
          ),
        )
        photo
      }!!
    }
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
    return motiveLocks.withLock(request.motiveId) { reserveSlot(request) }
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

  /**
   * Moves a photo to a different motive. When the album or security level changes, the photo-provider relocates
   * the files first (to a newly reserved slot if the album changes), then the move is committed.
   */
  fun move(
    photoId: UUID,
    request: PhotoMoveRequestDto,
    userSecurityLevel: SecurityLevelType,
  ): PhotoDto {
    if (userSecurityLevel != SecurityLevelType.FG) {
      throw AccessDeniedException("Only users with FG security level can move photos")
    }
    return motiveLocks.withLock(currentMotiveId(photoId), request.targetMotiveId) {
      moveLocked(photoId, request)
    }
  }

  private fun moveLocked(
    photoId: UUID,
    request: PhotoMoveRequestDto,
  ): PhotoDto {
    val photo =
      photoRepository.findById(photoId, SecurityLevelType.FG)
        ?: throw EntityNotFoundException("Photo $photoId not found")

    val targetMotive =
      motiveRepository.findById(request.targetMotiveId)
        ?: throw EntityNotFoundException("Motive ${request.targetMotiveId} not found")
    val targetAlbum =
      targetMotive.albumFor(photo.media)
        ?: throw EntityNotFoundException("Target motive ${request.targetMotiveId} has no ${kind(photo.media)} album")

    val placement = place(photo, targetMotive, targetAlbum)

    transactionTemplate.execute {
      photoRepository.move(
        photoId = photoId,
        motiveId = request.targetMotiveId,
        pageNumber = placement.pageNumber,
        imageNumber = placement.imageNumber,
        imageProd = placement.imageProd,
        imageWeb = placement.imageWeb,
        imageThumb = placement.imageThumb,
      )
      releaseReservation(placement)
    }

    return photoRepository.findById(photoId, SecurityLevelType.FG)
      ?: throw EntityNotFoundException("Photo $photoId not found after move")
  }

  /**
   * Updates the good-picture flag, gang and/or media of a photo. Fields that are null are left unchanged.
   * Changing to a media of the other kind (analog/digital) puts the photo in the motive's other album, so
   * the photo-provider relocates the files to a newly reserved slot there first, as in [move].
   */
  fun patch(
    photoId: UUID,
    request: PhotoPatchRequestDto,
    userSecurityLevel: SecurityLevelType,
  ): PhotoDto {
    if (userSecurityLevel != SecurityLevelType.FG) {
      throw AccessDeniedException("Only users with FG security level can edit photos")
    }
    return motiveLocks.withLock(currentMotiveId(photoId)) {
      patchLocked(photoId, request)
    }
  }

  private fun patchLocked(
    photoId: UUID,
    request: PhotoPatchRequestDto,
  ): PhotoDto {
    val photo =
      photoRepository.findById(photoId, SecurityLevelType.FG)
        ?: throw EntityNotFoundException("Photo $photoId not found")
    request.gangId?.let { gangRepository.findById(it) ?: throw EntityNotFoundException("Gang $it not found") }
    val media =
      request.mediaId?.let {
        mediaRepository.findOrNull(it) ?: throw EntityNotFoundException("Media $it not found")
      }

    val placement =
      media?.let {
        val album =
          photo.motive.albumFor(it)
            ?: throw EntityNotFoundException("Motive ${photo.motive.motiveId} has no ${kind(it)} album")
        place(photo, photo.motive, album)
      }

    transactionTemplate.execute {
      photoRepository.patch(photoId, request.goodPicture, request.gangId, request.mediaId)
      // The motive is unchanged, so the photo only gets a new slot when the media moved it to another album
      if (placement?.reservation != null) {
        photoRepository.move(
          photoId = photoId,
          motiveId = photo.motive.motiveId.id,
          pageNumber = placement.pageNumber,
          imageNumber = placement.imageNumber,
          imageProd = placement.imageProd,
          imageWeb = placement.imageWeb,
          imageThumb = placement.imageThumb,
        )
        releaseReservation(placement)
      }
    }

    return photoRepository.findById(photoId, SecurityLevelType.FG)
      ?: throw EntityNotFoundException("Photo $photoId not found after patch")
  }

  /**
   * Works out where the photo's files should live in [targetMotive]/[targetAlbum] and has the photo-provider
   * move them there. A different album gets a newly reserved slot, a security-level-only change keeps the
   * existing slot, and no change leaves the files alone.
   */
  private fun place(
    photo: PhotoDto,
    targetMotive: MotiveDto,
    targetAlbum: AlbumDto,
  ): PhotoPlacement {
    val currentAlbum =
      photo.motive.albumFor(photo.media)
        ?: throw EntityNotFoundException("Photo ${photo.photoId} has no ${kind(photo.media)} album")
    val sameAlbum = currentAlbum.albumId.id == targetAlbum.albumId.id
    val sameSecurity = photo.motive.securityLevel == targetMotive.securityLevel

    val reservation = if (sameAlbum) null else photoReservationService.createReservation(targetAlbum.albumId.id)
    val pageNumber = reservation?.pageNumber ?: photo.pageNumber
    val imageNumber = reservation?.imageNumber ?: photo.imageNumber

    val relocated =
      if (sameAlbum && sameSecurity) {
        null
      } else {
        try {
          photoProviderClient
            .relocate(
              listOf(
                PhotoLocation(
                  photoId = photo.photoId.toString(),
                  current = ImageUrls(prod = photo.imageProd, web = photo.imageWeb, thumb = photo.imageThumb),
                  securityLevel = targetMotive.securityLevel.securityLevelType.type,
                  album = targetAlbum.name,
                  pageNumber = pageNumber,
                  imageNumber = imageNumber,
                ),
              ),
            ).single()
        } catch (e: Exception) {
          // Files did not move, so the slot can be released
          reservation?.let { photoReservationService.deleteReservation(targetAlbum.albumId.id, pageNumber, imageNumber) }
          throw e
        }
      }

    return PhotoPlacement(
      reservation = reservation,
      albumId = targetAlbum.albumId.id,
      pageNumber = pageNumber,
      imageNumber = imageNumber,
      imageProd = relocated?.imageProd ?: photo.imageProd,
      imageWeb = relocated?.imageWeb ?: photo.imageWeb,
      imageThumb = relocated?.imageThumb ?: photo.imageThumb,
    )
  }

  private fun releaseReservation(placement: PhotoPlacement) {
    if (placement.reservation != null) {
      photoReservationService.deleteReservation(placement.albumId, placement.pageNumber, placement.imageNumber)
    }
  }

  private class PhotoPlacement(
    val reservation: PhotoReservationDto?,
    val albumId: UUID,
    val pageNumber: Int,
    val imageNumber: Int,
    val imageProd: String?,
    val imageWeb: String?,
    val imageThumb: String,
  )

  private fun reserveSlot(request: PhotoUploadRequestDto): PhotoReservationDto {
    val motive =
      motiveRepository.findById(request.motiveId)
        ?: throw EntityNotFoundException("Motive ${request.motiveId} not found")
    val media =
      mediaRepository.findOrNull(request.mediaId)
        ?: throw EntityNotFoundException("Media ${request.mediaId} not found")
    val album =
      motive.albumFor(media)
        ?: throw IllegalArgumentException("Motive ${request.motiveId} has no ${kind(media)} album")
    return photoReservationService.createReservation(album.albumId.id).copy(album = album)
  }

  private fun kind(media: MediaDto): String = if (media.analog) "analog" else "digital"

  private fun currentMotiveId(photoId: UUID): UUID =
    (
      photoRepository.findById(photoId, SecurityLevelType.FG)
        ?: throw EntityNotFoundException("Photo $photoId not found")
    ).motive.motiveId.id

  /**
   * Rejects files the photo-provider wrote under a security level the motive no longer has,
   * e.g. an upload reserved before a concurrent relocation changed the motive's level.
   */
  private fun requireCurrentSecurityLevel(
    writtenUnder: String,
    motive: MotiveDto,
  ) {
    val current = motive.securityLevel.securityLevelType.type
    if (!writtenUnder.equals(current, ignoreCase = true)) {
      throw IllegalArgumentException("Files were stored under security level $writtenUnder, but the motive is now $current")
    }
  }

  private fun validate(
    request: PhotoUploadRequestDto,
    username: String,
  ): List<String> {
    val errors = mutableListOf<String>()
    val motive = motiveRepository.findById(request.motiveId)
    val media = mediaRepository.findOrNull(request.mediaId)
    if (motive == null) errors.add("Motive with id ${request.motiveId} does not exist")
    if (media == null) errors.add("Media with id ${request.mediaId} does not exist")
    if (motive != null && media != null && motive.albumFor(media) == null) {
      errors.add("Motive has no ${kind(media)} album")
    }
    if (photoGangBangerRepository.findByUsername(username) == null) {
      errors.add("No PhotoGangBanger found for user '$username'")
    }
    return errors.toList()
  }
}

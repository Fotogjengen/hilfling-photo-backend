package no.fg.hilflingbackend.service

import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.dto.albumFor
import no.fg.hilflingbackend.repository.PhotoRepository
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import java.util.UUID

/**
 * Has the photo-provider move a motive's photo files to where its new state says they belong, then commits.
 *
 * Files move before the DB commits, so a tightened security level never leaves files in a more permissive folder.
 * If the commit fails after the move, retrying the same change completes it: the provider treats already-moved
 * files as done. Callers must hold the motive's lock in [MotiveLocks].
 */
@Service
class PhotoRelocationService(
  private val photoRepository: PhotoRepository,
  private val photoProviderClient: PhotoProviderClient,
  private val transactionTemplate: TransactionTemplate,
) {
  fun <T> relocateMotive(
    target: MotiveDto,
    commit: () -> T,
  ): T {
    val locations =
      photoRepository
        .findByMotiveId(target.motiveId.id, SecurityLevelType.FG)
        .map { locationOf(it, target) }
    val relocated = photoProviderClient.relocate(locations).associateBy { it.photoId }
    val missing = locations.map { it.photoId }.filterNot { it in relocated }
    check(missing.isEmpty()) { "Photo-provider did not relocate photos $missing" }

    return transactionTemplate.execute {
      val result = commit()
      locations.map { relocated.getValue(it.photoId) }.forEach {
        photoRepository.updateImageUrls(
          photoId = UUID.fromString(it.photoId),
          imageProd = it.imageProd,
          imageWeb = it.imageWeb,
          imageThumb = it.imageThumb,
        )
      }
      result
    }!!
  }

  private fun locationOf(
    photo: PhotoDto,
    target: MotiveDto,
  ): PhotoLocation {
    val album = target.albumFor(photo.media) ?: error("Motive ${target.motiveId} has no album for photo ${photo.photoId}")
    return PhotoLocation(
      photoId = photo.photoId.id.toString(),
      current = ImageUrls(prod = photo.imageProd, web = photo.imageWeb, thumb = photo.imageThumb),
      securityLevel = target.securityLevel.securityLevelType.type,
      album = album.name,
      pageNumber = photo.pageNumber,
      imageNumber = photo.imageNumber,
    )
  }
}

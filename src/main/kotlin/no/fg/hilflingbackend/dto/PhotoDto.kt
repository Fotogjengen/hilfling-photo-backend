package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.Photo
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class PhotoUploadRequestDto(
  val goodPicture: Boolean = false,
  val analog: Boolean = false,
  val motiveId: UUID,
  val gangId: UUID?,
  val dateTaken: LocalDate,
)

data class PhotoGoodPictureToggleRequestDto(
  val photoId: UUID,
  val goodPicture: Boolean,
)

data class PhotoFinalizeRequestDto(
  val goodPicture: Boolean = false,
  val analog: Boolean = false,
  val pageNumber: Int,
  val imageNumber: Int,
  val imageProd: String?,
  val imageWeb: String,
  val imageThumb: String,
  val motiveId: UUID,
  val gangId: UUID?,
  val dateTaken: LocalDate,
)

data class PhotoPatchRequestDto(
  val photoId: PhotoId,
  val goodPicture: Boolean?,
  val analog: Boolean?,
  val motive: MotiveDto?,
  val gang: GangDto?,
  val photoGangBangerDto: PhotoGangBangerDto?,
  val photoTags: List<String>?,
)

data class PhotoMoveRequestDto(
  val targetMotiveId: UUID,
)

/**
 * Response of POST /photos/{id}/move/reserve.
 *
 * The photo-provider uses this to decide whether the on-disk files must be
 * relocated and, if so, where to move them. When [fileMoveRequired] is false
 * the target motive lives in the same album with the same security level as
 * the photo's current motive, so only the `motive_id` column changes and the
 * files stay put. In that case the proxy finalises with the unchanged URLs and
 * slot echoed back here.
 *
 * When [fileMoveRequired] is true, [albumName]/[pageNumber]/[imageNumber]/
 * [securityLevel] describe the destination path the proxy must build, and the
 * `currentImage*` fields point at the existing files to move.
 */
data class PhotoMoveReserveResponseDto(
  val fileMoveRequired: Boolean,
  val albumName: String,
  val pageNumber: Int,
  val imageNumber: Int,
  val securityLevel: String,
  val currentImageProd: String?,
  val currentImageWeb: String?,
  val currentImageThumb: String,
)

data class PhotoMoveFinalizeRequestDto(
  val targetMotiveId: UUID,
  val pageNumber: Int,
  val imageNumber: Int,
  val imageProd: String?,
  val imageWeb: String,
  val imageThumb: String,
)

data class PhotoDto(
  val photoId: PhotoId = PhotoId(),
  val goodPicture: Boolean = false,
  val analog: Boolean = false,
  val imageNumber: Int,
  val pageNumber: Int,
  val imageProd: String?,
  val imageWeb: String?,
  val imageThumb: String,
  val motive: MotiveDto,
  val securityLevel: SecurityLevelDto = motive.securityLevel,
  val gang: GangDto?,
  val photoGangBangerDto: PhotoGangBangerDto,
  val dateUploaded: LocalDate,
) {
  fun toEntity(): Photo {
    val photo = this
    return Photo {
      this.id = photo.photoId.id
      this.goodPicture = photo.goodPicture
      this.analog = photo.analog
      this.imageNumber = photo.imageNumber
      this.pageNumber = photo.pageNumber
      this.securityLevel = photo.securityLevel.securityLevelType.type
      this.imageProd = photo.imageProd
      this.imageWeb = photo.imageWeb
      this.imageThumb = photo.imageThumb
      this.dateCreated = LocalDateTime.now()
      this.motive = photo.motive.toEntity()
      this.gang = photo.gang?.toEntity()
      this.photoGangBanger = photo.photoGangBangerDto.toEntity()
    }
  }
}

data class PhotoId(
  override val id: UUID = UUID.randomUUID(),
) : UuidId {
  override fun toString(): String = id.toString()
}

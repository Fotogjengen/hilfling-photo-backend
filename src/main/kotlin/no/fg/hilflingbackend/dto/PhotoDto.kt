package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.Photo
import no.fg.hilflingbackend.valueobject.ImageFileName
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import java.time.LocalDate
import java.util.UUID

data class PhotoUploadRequestDto(
  val goodPicture: Boolean = false,
  val analog: Boolean = false,
  val motive: MotiveDto,
  val gang: GangDto?,
  val photoGangBangerDto: PhotoGangBangerDto,
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
  val dateTaken: LocalDate,
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
      this.dateCreated = photo.dateTaken
      this.motive = photo.motive.toEntity()
      this.gang = photo.gang?.toEntity()
      this.photoGangBanger = photo.photoGangBangerDto.toEntity()
    }
  }

  companion object {
    fun createWithFileName(
      fileName: ImageFileName,
      goodPicture: Boolean,
      analog: Boolean = false,
      imageNumber: Int,
      pageNumber: Int,
      motive: MotiveDto,
      gang: GangDto? = null,
      photoGangBangerDto: PhotoGangBangerDto,
      dateTaken: LocalDate,
    ): Pair<PhotoDto, ImageFileName> {
      val photoId = PhotoId()
      val newUniqueFileName = ImageFileName("${photoId}${fileName.getFileExtension()}")

      return Pair(
        PhotoDto(
          photoId = photoId,
          goodPicture = goodPicture,
          analog = analog,
          imageNumber = imageNumber,
          pageNumber = pageNumber,
          imageProd = null,
          imageWeb = null,
          imageThumb = newUniqueFileName.filename,
          motive = motive,
          gang = gang,
          photoGangBangerDto = photoGangBangerDto,
          dateTaken = dateTaken,
        ),
        newUniqueFileName,
      )
    }
  }
}

data class PhotoId(
  override val id: UUID = UUID.randomUUID(),
) : UuidId {
  override fun toString(): String = id.toString()
}

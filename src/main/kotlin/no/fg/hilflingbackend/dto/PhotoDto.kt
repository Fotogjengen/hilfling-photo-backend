package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.Motive
import no.fg.hilflingbackend.model.Photo
import no.fg.hilflingbackend.service.PhotoService
import no.fg.hilflingbackend.value_object.ImageFileName
import no.fg.hilflingbackend.value_object.PhotoSize
import java.nio.file.Path
import java.nio.file.Paths
import java.util.UUID

data class PhotoDto(
  val photoId: PhotoId = PhotoId(),
  val isGoodPicture: Boolean = false,
  val smallUrl: String,
  val mediumUrl: String,
  val largeUrl: String,

  val motive: Motive,
  val placeDto: PlaceDto,
  val securityLevel: SecurityLevelDto,
  val gang: GangDto,
  val photoGangBangerDto: PhotoGangBangerDto
) {
  fun toEntity(): Photo {
    val photo = this
    return Photo {
      this.id = photo.photoId.id
      this.isGoodPicture = photo.isGoodPicture
      this.smallUrl = photo.smallUrl
      this.mediumUrl = photo.mediumUrl
      this.largeUrl = photo.largeUrl
      this.motive = photo.motive
      this.place = photo.placeDto.toEntity()
      this.securityLevel = photo.securityLevel.toEntity()
      this.gang = photo.gang.toEntity()
      this.photoGangBanger = photo.photoGangBangerDto.toEntity()
    }
  }

  fun getFilePath(
    fileName: ImageFileName = ImageFileName(this.largeUrl),
    securityLevel: SecurityLevelDto = this.securityLevel,
    basePath: String,
    size: PhotoSize,
  ): Path  =  PhotoService.generateFilePath(
      fileName,
      securityLevel,
      size
    )
  companion object {
    fun createWithFileName(
      fileName: ImageFileName,
      isGoodPicture: Boolean,
      motive: Motive,
      placeDto: PlaceDto,
      securityLevel: SecurityLevelDto,
      gang: GangDto,
      photoGangBangerDto: PhotoGangBangerDto
    ): PhotoDto {
      // Generate unique ID
      val photoId = PhotoId()
      val newUniqueFileName = ImageFileName("${photoId}${fileName.getFileExtension()}")

      return PhotoDto(
        photoId = photoId,
        isGoodPicture = isGoodPicture,
        smallUrl = newUniqueFileName.filename,
        mediumUrl = newUniqueFileName.filename,
        largeUrl = newUniqueFileName.filename,
        motive = motive,
        placeDto = placeDto,
        gang = gang,
        securityLevel = securityLevel,
        photoGangBangerDto = photoGangBangerDto
      )
    }
  }
}

data class PhotoId(
  override val id: UUID = UUID.randomUUID()
) : UuidId {
  override fun toString(): String = id.toString()
}

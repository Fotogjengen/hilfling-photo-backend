package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.Photo
import no.fg.hilflingbackend.value_object.ImageFileName
import java.util.UUID

data class PhotoPatchRequestDto(
  val photoId: PhotoId,
  val isGoodPicture: Boolean?,
  val smallUrl: String?,
  val mediumUrl: String?,
  val largeUrl: String?,
  val motive: MotiveDto?,
  val placeDto: PlaceDto?,
  val securityLevel: SecurityLevelDto?,
  val gang: GangDto?,
  val albumDto: AlbumDto?,
  val categoryDto: CategoryDto?,
  val photoGangBangerDto: PhotoGangBangerDto?,
  val photoTags: List<PhotoTagDto>?
)

data class PhotoDto(
  val photoId: PhotoId = PhotoId(),
  val isGoodPicture: Boolean = false,
  val smallUrl: String,
  val mediumUrl: String,
  val largeUrl: String,

  val motive: MotiveDto,
  val placeDto: PlaceDto,
  val securityLevel: SecurityLevelDto,
  val gang: GangDto?,
  val albumDto: AlbumDto,
  val categoryDto: CategoryDto,
  val photoGangBangerDto: PhotoGangBangerDto,
  val photoTags: List<PhotoTagDto>
) {
  fun toEntity(): Photo {
    val photo = this
    return Photo {
      this.id = photo.photoId.id
      this.isGoodPicture = photo.isGoodPicture
      this.smallUrl = photo.smallUrl
      this.mediumUrl = photo.mediumUrl
      this.largeUrl = photo.largeUrl
      this.motive = photo.motive.toEntity()
      this.place = photo.placeDto.toEntity()
      this.securityLevel = photo.securityLevel.toEntity()
      // this.gang = photo.gang.toEntity() as Gang
      this.album = photo.albumDto.toEntity()
      this.category = photo.categoryDto.toEntity()
      this.photoGangBanger = photo.photoGangBangerDto.toEntity()
    }
  }
  companion object {
    fun createWithFileName(
      fileName: ImageFileName,
      isGoodPicture: Boolean,
      motive: MotiveDto,
      placeDto: PlaceDto,
      securityLevel: SecurityLevelDto,
      gang: GangDto? = null,
      albumDto: AlbumDto,
      photoTags: List<PhotoTagDto>,
      categoryDto: CategoryDto,
      photoGangBangerDto: PhotoGangBangerDto,
    ): Pair<PhotoDto, ImageFileName> {
      // Generate unique ID
      val photoId = PhotoId()
      val newUniqueFileName = ImageFileName("${photoId}${fileName.getFileExtension()}")

      return Pair(
        PhotoDto(
          photoId = photoId,
          isGoodPicture = isGoodPicture,
          smallUrl = newUniqueFileName.filename,
          mediumUrl = newUniqueFileName.filename,
          largeUrl = newUniqueFileName.filename,
          motive = motive,
          placeDto = placeDto,
          gang = gang,
          albumDto = albumDto,
          categoryDto = categoryDto,
          securityLevel = securityLevel,
          photoGangBangerDto = photoGangBangerDto,
          photoTags = photoTags
        ),
        newUniqueFileName
      )
    }
  }
}

data class PhotoId(
  override val id: UUID = UUID.randomUUID()
) : UuidId {
  override fun toString(): String = id.toString()
}

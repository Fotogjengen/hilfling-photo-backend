package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.Motive
import no.fg.hilflingbackend.model.Photo
import no.fg.hilflingbackend.model.Place
import no.fg.hilflingbackend.model.SecurityLevel
import no.fg.hilflingbackend.value_object.ImageFileName
import org.slf4j.LoggerFactory
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
  val securityLevel: SecurityLevel,
  val gang: GangDto,
  val photoGangBangerDto: PhotoGangBangerDto
) {
  val logger = LoggerFactory.getLogger(this::class.java)

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
      this.securityLevel = photo.securityLevel
      this.gang = photo.gang.toEntity()
      this.photoGangBanger = photo.photoGangBangerDto.toEntity()
    }
  }
  companion object {
    fun createPhotoDtoAndSaveToDisk(
      fileName: ImageFileName,
      isGoodPicture: Boolean,
      motive: Motive,
      placeDto: PlaceDto,
      securityLevel: SecurityLevel,
      gang: GangDto,
      photoGangBangerDto: PhotoGangBangerDto
    ): Pair<PhotoDto, Path> {
      // Generate unique ID
      val photoId = PhotoId()
      val newUniqueFileName = ImageFileName("$photoId${fileName.getFileExtension()}")
      val filePath = generateFilePath(newUniqueFileName, securityLevel)
      // TODO: generateFilePath for mediam and small
      // TODO: Find a less hacky way of shortening the path
      val relativeFilePath = filePath.toString().subSequence(20, filePath.toString().length).toString()

      return (
        PhotoDto(
          photoId = photoId,
          isGoodPicture = isGoodPicture,
          smallUrl = relativeFilePath,
          mediumUrl = relativeFilePath,
          largeUrl = relativeFilePath,
          motive = motive,
          placeDto = placeDto,
          gang = gang,
          securityLevel = securityLevel,
          photoGangBangerDto = photoGangBangerDto
        ) to filePath
        )
    }
    fun generateFilePath(fileName: ImageFileName, securityLevel: SecurityLevel): Path {
      // TODO: Move rootLocation to a config
      val rootLocation = Paths.get("static-files/static/img/")
      val photoGangBangerLocation = Paths.get("static-files/static/img//FG")
      val houseMemberLocation = Paths.get("static-files/static/img//HUSFOLK")
      val allLocation = Paths.get("static-files/static/img//ALLE")
      return when (securityLevel.type) {
        "FG" -> photoGangBangerLocation.resolve(fileName.filename)
        "HUSFOLK" -> houseMemberLocation.resolve(fileName.filename)
        "ALLE" -> allLocation.resolve(fileName.filename)
        else -> throw IllegalArgumentException("Invalid security level")
      }
    }
  }
}

data class PhotoId(
  override val id: UUID = UUID.randomUUID()
) : UuidId {
  override fun toString(): String = id.toString()
}

package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.Gang
import no.fg.hilflingbackend.model.Motive
import no.fg.hilflingbackend.model.Photo
import no.fg.hilflingbackend.model.Place
import no.fg.hilflingbackend.model.SecurityLevel
import java.util.UUID

data class PhotoDto(
  val photoId: PhotoId = PhotoId(),
  val isGoodPicture: Boolean = false,
  val smallUrl: String,
  val mediumUrl: String,
  val largeUrl: String,

  val motive: Motive,
  val place: Place,
  val securityLevel: SecurityLevel,
  val gang: Gang,
  val photoGangBangerDto: PhotoGangBangerDto
)

data class PhotoId(
  override val id: UUID = UUID.randomUUID()
) : UuidId {
  override fun toString(): String = id.toString()
}

fun PhotoDto.toEntity(): Photo {
  val photo = this
  return Photo {
    this.id = photo.photoId.id
    this.isGoodPicture = photo.isGoodPicture
    this.smallUrl = photo.smallUrl
    this.mediumUrl = photo.mediumUrl
    this.largeUrl = photo.largeUrl
    this.motive = photo.motive
    this.place = photo.place
    this.securityLevel = photo.securityLevel
    this.gang = photo.gang
    this.photoGangBanger = photo.photoGangBangerDto.toEntity()
  }
}

package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.*
import java.util.*

data class PhotoDto(
  val photoId: PhotoId = PhotoId(),
  val isGoodPicture: Boolean = false,
  // TODO: Fix
  val smallUrl: String = "",
  // TODO: Fix
  val mediumUrl: String = "",
  // TODO: Fix
  val largeUrl: String = "",

  val motive: Motive,
  val place: Place,
  val securityLevel: SecurityLevel,
  val gang: Gang,
  val photoGangBanger: PhotoGangBanger
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
      this.photoGangBanger = photo.photoGangBanger
  }
}

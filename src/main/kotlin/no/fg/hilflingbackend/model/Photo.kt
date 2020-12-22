package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.boolean
import me.liuwj.ktorm.schema.uuid
import me.liuwj.ktorm.schema.varchar
import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.dto.PhotoId

interface Photo : BaseModel<Photo> {
  companion object : Entity.Factory<Photo>()

  var isGoodPicture: Boolean

  var smallUrl: String
  var mediumUrl: String
  var largeUrl: String

  // Foreign keys
  var motive: Motive
  var place: Place
  var securityLevel: SecurityLevel
  var gang: Gang
  var photoGangBanger: PhotoGangBanger
}

fun Photo.toDto(): PhotoDto = PhotoDto(
  photoId = PhotoId(this.id),
  isGoodPicture = this.isGoodPicture,
  smallUrl = this.smallUrl,
  mediumUrl = this.mediumUrl,
  largeUrl = this.largeUrl,
  motive = this.motive,
  place = this.place,
  securityLevel = this.securityLevel,
  gang = this.gang.toDto(),
  photoGangBangerDto = this.photoGangBanger.toDto()
)

object Photos : BaseTable<Photo>("photo") {
  val isGoodPicture = boolean("is_good_picture").bindTo { it.isGoodPicture }

  val smallUrl = varchar("small_url").bindTo { it.smallUrl }
  val mediumUrl = varchar("medium_url").bindTo { it.mediumUrl }
  val largeUrl = varchar("large_url").bindTo { it.largeUrl }

  // Foreign keys
  val motiveId = uuid("motive_id").references(Motives) { it.motive }
  val placeId = uuid("place_id").references(Places) { it.place }
  val securityLevelId = uuid("security_level_id").references(SecurityLevels) { it.securityLevel }
  val gangId = uuid("gang_id").references(Gangs) { it.gang }
  val photoGangBangerId = uuid("photo_gang_banger_id").references(PhotoGangBangers) { it.photoGangBanger }
}

val Database.photos get() = this.sequenceOf(Photos)

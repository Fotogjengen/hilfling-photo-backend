package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.boolean
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.uuid
import me.liuwj.ktorm.schema.varchar
import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.dto.PhotoId
import no.fg.hilflingbackend.dto.SecurityLevelDto
import no.fg.hilflingbackend.valueobject.SecurityLevelType

interface Photo : BaseModel<Photo> {
  companion object : Entity.Factory<Photo>()

  var goodPicture: Boolean
  var analog: Boolean
  var imageNumber: Int
  var pageNumber: Int

  var securityLevel: String
  var imageProd: String?
  var imageWeb: String?
  var imageThumb: String

  // Foreign keys
  var motive: Motive
  var gang: Gang?
  var photoGangBanger: PhotoGangBanger
}

object Photos : BaseTable<Photo>("photo") {
  val goodPicture = boolean("good_picture").bindTo { it.goodPicture }
  val analog = boolean("analog").bindTo { it.analog }
  val imageNumber = int("image_number").bindTo { it.imageNumber }
  val pageNumber = int("page_number").bindTo { it.pageNumber }

  val securityLevel = varchar("security_level").bindTo { it.securityLevel }
  val imageProd = varchar("image_prod").bindTo { it.imageProd }
  val imageWeb = varchar("image_web").bindTo { it.imageWeb }
  val imageThumb = varchar("image_thumb").bindTo { it.imageThumb }

  // Foreign keys
  val motiveId = uuid("motive_id").references(Motives) { it.motive }
  val gangId = uuid("gang_id").references(Gangs) { it.gang }
  val photoGangBangerId =
    uuid("photo_gang_banger_id").references(PhotoGangBangers) { it.photoGangBanger }
}

val Database.photos
  get() = this.sequenceOf(Photos)

fun Photo.toDto(): PhotoDto =
  PhotoDto(
    photoId = PhotoId(this.id),
    goodPicture = this.goodPicture,
    analog = this.analog,
    imageNumber = this.imageNumber,
    pageNumber = this.pageNumber,
    securityLevel = SecurityLevelDto(SecurityLevelType.valueOf(this.securityLevel)),
    imageProd = this.imageProd,
    imageWeb = this.imageWeb,
    imageThumb = this.imageThumb,
    motive = this.motive.toDto(),
    photoGangBangerDto = this.photoGangBanger.toDto(),
    gang = this.gang?.toDto(),
    dateTaken = this.dateCreated.toLocalDate(),
  )

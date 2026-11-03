package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.EntitySequence
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.boolean
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.uuid
import me.liuwj.ktorm.schema.varchar
import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.dto.PhotoId
import no.fg.hilflingbackend.valueobject.SecurityLevelType

interface Photo : BaseModel<Photo> {
  companion object : Entity.Factory<Photo>()

  var goodPicture: Boolean
  var imageNumber: Int
  var pageNumber: Int

  var imageProd: String?
  var imageWeb: String?
  var imageThumb: String?

  // Foreign keys
  var motive: Motive
  var media: Media
  var gang: Gang?
  var photoGangBanger: PhotoGangBanger?
}

open class PhotoTable(
  withPhotographer: Boolean,
) : BaseTable<Photo>("photo") {
  val goodPicture = boolean("good_picture").bindTo { it.goodPicture }
  val imageNumber = int("image_number").bindTo { it.imageNumber }
  val pageNumber = int("page_number").bindTo { it.pageNumber }

  val imageProd = varchar("image_prod").bindTo { it.imageProd }
  val imageWeb = varchar("image_web").bindTo { it.imageWeb }
  val imageThumb = varchar("image_thumb").bindTo { it.imageThumb }

  // Foreign keys
  val motiveId = uuid("motive_id").references(Motives) { it.motive }
  val mediaId = uuid("media_id").references(Medias) { it.media }
  val gangId = uuid("gang_id").references(Gangs) { it.gang }
  val photoGangBangerId =
    uuid("photo_gang_banger_id").let { column ->
      if (withPhotographer) column.references(PhotoGangBangers) { it.photoGangBanger } else column
    }
}

object Photos : PhotoTable(withPhotographer = true)

// Never joins the photographer, so photoGangBanger stays null
object PhotosWithoutPhotographer : PhotoTable(withPhotographer = false)

val Database.photos
  get() = this.sequenceOf(Photos)

/** Photos as the user may see them: only FG users get the photographer, for the others it is not queried at all */
fun Database.photosFor(userSecurityLevel: SecurityLevelType): EntitySequence<Photo, PhotoTable> = this.sequenceOf(if (userSecurityLevel == SecurityLevelType.FG) Photos else PhotosWithoutPhotographer)

fun Photo.toDto(): PhotoDto =
  PhotoDto(
    photoId = PhotoId(this.id),
    goodPicture = this.goodPicture,
    imageNumber = this.imageNumber,
    pageNumber = this.pageNumber,
    imageProd = this.imageProd,
    imageWeb = this.imageWeb,
    imageThumb = this.imageThumb,
    motive = this.motive.toDto(),
    media = this.media.toDto(),
    photoGangBangerDto = this.photoGangBanger?.toDto(),
    gang = this.gang?.toDto(),
    dateUploaded = this.dateCreated.toLocalDate(),
  )

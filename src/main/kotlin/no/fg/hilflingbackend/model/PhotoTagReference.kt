package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.uuid
import me.liuwj.ktorm.schema.varchar
import java.util.UUID

interface  PhotoTagReference: BaseModel<PhotoTagReference> {
  companion object : Entity.Factory<PhotoTagReference>()

  var photoTag: UUID
  var photo: UUID
}

object PhotoTagReferences: BaseTable<PhotoTagReference>("photo_tag_in_photo") {
  val photoTagId = uuid("photo_tag_id").bindTo{ it.photoTag}
  val photoId = uuid("photo_id").bindTo{ it.photo}

}

val Database.photo_tag_references get() = this.sequenceOf(PhotoTagReferences)

package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.uuid
import java.util.UUID

interface PhotoTagReference : BaseModel<PhotoTagReference> {
  companion object : Entity.Factory<PhotoTagReference>()

  var photoTag: PhotoTag
  var photo: Photo
}

object PhotoTagReferences : BaseTable<PhotoTagReference>("photo_tag_in_photo") {
  val photoTagId = uuid("photo_tag_id").references(PhotoTags) { it.photoTag }
  val photoId = uuid("photo_id").references(Photos) { it.photo }
}

val Database.photo_tag_references get() = this.sequenceOf(PhotoTagReferences)

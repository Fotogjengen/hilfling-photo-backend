package no.fg.hilflingbackend.model

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.uuid

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

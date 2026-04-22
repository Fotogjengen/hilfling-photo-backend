package no.fg.hilflingbackend.model

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.int

interface AnalogPhoto : BaseModel<AnalogPhoto> {
  companion object : Entity.Factory<AnalogPhoto>()

  var imageNumber: Int
  var pageNumber: Int

  // Foreign keys
  var photo: Photo
}

object AnalogPhotos : BaseTable<AnalogPhoto>("analog_photos") {
  val imageNumber = int("image_number").bindTo { it.imageNumber }
  val pageNumber = int("page_number").bindTo { it.pageNumber }

  // Foreign keys
  val photo = int("photo_id").references(Photos) { it.photo }
}

val Database.analog_photos get() = this.sequenceOf(AnalogPhotos)

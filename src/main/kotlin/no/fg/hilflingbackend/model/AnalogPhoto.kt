package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.uuid

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
  val photo = uuid("photo_id").references(Photos) { it.photo }
}

val Database.analog_photos get() = this.sequenceOf(AnalogPhotos)

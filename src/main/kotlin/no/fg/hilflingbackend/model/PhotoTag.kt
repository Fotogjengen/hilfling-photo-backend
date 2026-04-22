package no.fg.hilflingbackend.model

import no.fg.hilflingbackend.dto.PhotoTagDto
import no.fg.hilflingbackend.dto.PhotoTagId
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.varchar

interface PhotoTag : BaseModel<PhotoTag> {
  companion object : Entity.Factory<PhotoTag>()

  var name: String
}

object PhotoTags : BaseTable<PhotoTag>("photo_tag") {
  val name = varchar("name").bindTo { it.name }
}

fun PhotoTag.toDto() = PhotoTagDto(
  photoTagId = PhotoTagId(id),
  name = name

)

val Database.photo_tags get() = this.sequenceOf(PhotoTags)

package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.varchar
import no.fg.hilflingbackend.dto.PhotoTagDto
import no.fg.hilflingbackend.dto.PhotoTagId
import java.util.UUID

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

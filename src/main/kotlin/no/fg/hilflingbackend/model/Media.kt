package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.boolean
import me.liuwj.ktorm.schema.varchar
import no.fg.hilflingbackend.dto.MediaDto
import no.fg.hilflingbackend.dto.MediaId

interface Media : BaseModel<Media> {
  companion object : Entity.Factory<Media>()

  var medium: String
  var analog: Boolean
}

fun Media.toDto(): MediaDto =
  MediaDto(
    mediaId = MediaId(this.id),
    medium = this.medium,
    analog = this.analog,
  )

object Medias : BaseTable<Media>("media") {
  val medium = varchar("medium").bindTo { it.medium }
  val analog = boolean("analog").bindTo { it.analog }
}

val Database.medias get() = this.sequenceOf(Medias)

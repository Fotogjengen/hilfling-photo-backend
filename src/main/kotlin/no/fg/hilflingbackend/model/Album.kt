package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.boolean
import me.liuwj.ktorm.schema.varchar
import no.fg.hilflingbackend.dto.AlbumDto
import no.fg.hilflingbackend.dto.AlbumId

interface Album : BaseModel<Album> {
  companion object : Entity.Factory<Album>()

  var name: String
  var description: String?
  var analog: Boolean
}

fun Album.toDto(): AlbumDto =
  AlbumDto(
    albumId = AlbumId(this.id),
    name = this.name,
    description = this.description,
    analog = this.analog,
  )

object Albums : BaseTable<Album>("album") {
  val name = varchar("name").bindTo { it.name }
  val description = varchar("description").bindTo { it.description }
  val analog = boolean("analog").bindTo { it.analog }
}

val Database.albums
  get() = this.sequenceOf(Albums)

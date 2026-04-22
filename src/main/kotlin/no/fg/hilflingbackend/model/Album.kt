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

  var title: String
  var analog: Boolean
}

fun Album.toDto(): AlbumDto = AlbumDto(
  albumId = AlbumId(this.id),
  title = this.title,
  analog = this.analog
)

object Albums : BaseTable<Album>("album") {
  val title = varchar("title").bindTo { it.title }
  val analog = boolean("is_analog").bindTo { it.analog }
}

val Database.albums get() = this.sequenceOf(Albums)

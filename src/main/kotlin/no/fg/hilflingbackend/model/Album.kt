package no.fg.hilflingbackend.model

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.boolean
import org.ktorm.schema.varchar
import no.fg.hilflingbackend.dto.AlbumDto
import no.fg.hilflingbackend.dto.AlbumId

interface Album : BaseModel<Album> {
  companion object : Entity.Factory<Album>()

  var title: String
  var isAnalog: Boolean
}

fun Album.toDto(): AlbumDto = AlbumDto(
  albumId = AlbumId(this.id),
  title = this.title,
  isAnalog = this.isAnalog
)

object Albums : BaseTable<Album>("album") {
  val title = varchar("title").bindTo { it.title }
  val isAnalog = boolean("is_analog").bindTo { it.isAnalog }
}

val Database.albums get() = this.sequenceOf(Albums)

package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.uuid
import me.liuwj.ktorm.schema.varchar
import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.dto.MotiveId
import no.fg.hilflingbackend.dto.SearchDto

interface Search : BaseModel<Search> {
  companion object : Entity.Factory<Motive>()

  var title: String

  // Foreign keys
  var category: Category
  var eventOwner: EventOwner
  var album: Album
}

fun Search.toDto(): SearchDto = SearchDto(
  title = this.title,
  categoryDto = this.category.toDto(),
  eventOwnerDto = eventOwner.toDto(),
  albumDto = album.toDto()
)

val Database.searches get() = this.sequenceOf(Motives)

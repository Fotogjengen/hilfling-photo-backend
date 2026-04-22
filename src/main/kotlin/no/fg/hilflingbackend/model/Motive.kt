package no.fg.hilflingbackend.model

import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.dto.MotiveId
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.uuid
import org.ktorm.schema.varchar

interface Motive : BaseModel<Motive> {
  companion object : Entity.Factory<Motive>()

  var title: String

  // Foreign keys
  var category: Category
  var eventOwner: EventOwner
  var album: Album
}

fun Motive.toDto(): MotiveDto = MotiveDto(
  motiveId = MotiveId(this.id),
  title = this.title,
  categoryDto = this.category.toDto(),
  eventOwnerDto = eventOwner.toDto(),
  albumDto = album.toDto(),
  dateCreated = this.dateCreated
)

object Motives : BaseTable<Motive>("motive") {
  val title = varchar("title").bindTo { it.title }

  // Foreign keys
  val categoryId = uuid("category_id").references(Categories) { it.category }
  val eventOwnerId = uuid("event_owner_id").references(EventOwners) { it.eventOwner }
  val albumId = uuid("album_id").references(Albums) { it.album }
}

val Database.motives get() = this.sequenceOf(Motives)

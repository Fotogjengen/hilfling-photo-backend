package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar

interface Motive : BaseModel<Motive> {
  companion object : Entity.Factory<Motive>()

  var title: String

  // Foreign keys
  var category: Category
  var eventOwner: EventOwner
  var album: Album
}

object Motives : BaseTable<Motive>("motive") {
  val title = varchar("title").bindTo { it.title }

  // Foreign keys
  val categoryId = int("category_id").references(Categories) { it.category }
  val eventOwnerId = int("event_owner_id").references(EventOwners) { it.eventOwner }
  val albumId = int("album_id").references(Albums) { it.album }
}

val Database.motives get() = this.sequenceOf(Motives)

package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.date
import me.liuwj.ktorm.schema.uuid
import me.liuwj.ktorm.schema.varchar
import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.dto.MotiveId
import no.fg.hilflingbackend.dto.SecurityLevelDto
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import java.time.LocalDate

interface Motive : BaseModel<Motive> {
  companion object : Entity.Factory<Motive>()

  var title: String
  var date: LocalDate
  var securityLevel: String

  // Foreign keys
  var category: Category
  var eventOwner: EventOwner
  var place: Place
  var album: Album?
  var analogAlbum: Album?
}

fun Motive.toDto(): MotiveDto =
  MotiveDto(
    motiveId = MotiveId(this.id),
    title = this.title,
    date = this.date,
    categoryDto = this.category.toDto(),
    eventOwnerDto = this.eventOwner.toDto(),
    placeDto = this.place.toDto(),
    securityLevel = SecurityLevelDto(SecurityLevelType.valueOf(this.securityLevel)),
    albumDto = album?.toDto(),
    analogAlbumDto = analogAlbum?.toDto(),
    dateCreated = this.dateCreated,
  )

object Motives : BaseTable<Motive>("motive") {
  val title = varchar("title").bindTo { it.title }
  val date = date("date").bindTo { it.date }
  val securityLevel = varchar("security_level").bindTo { it.securityLevel }

  // Foreign keys
  val categoryId = uuid("category_id").references(Categories) { it.category }
  val eventOwnerId = uuid("event_owner_id").references(EventOwners) { it.eventOwner }
  val placeId = uuid("place_id").references(Places) { it.place }
  val albumId = uuid("album_id").references(Albums) { it.album }
  val analogAlbumId = uuid("analog_album_id").references(Albums) { it.analogAlbum }
}

val Database.motives get() = this.sequenceOf(Motives)

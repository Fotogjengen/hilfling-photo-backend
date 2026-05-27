package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.datetime
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.uuid
import no.fg.hilflingbackend.dto.AlbumId
import no.fg.hilflingbackend.dto.PhotoReservationDto
import java.time.LocalDateTime

interface PhotoReservation : Entity<PhotoReservation> {
  companion object : Entity.Factory<PhotoReservation>()

  var album: Album
  var pageNumber: Int
  var imageNumber: Int
  var reservedAt: LocalDateTime
}

fun PhotoReservation.toDto(): PhotoReservationDto =
  PhotoReservationDto(
    albumId = AlbumId(this.album.id),
    pageNumber = this.pageNumber,
    imageNumber = this.imageNumber,
    reservedAt = this.reservedAt,
  )

object PhotoReservations : Table<PhotoReservation>("photo_reservation") {
  val albumId = uuid("album_id").primaryKey().references(Albums) { it.album }
  val pageNumber = int("page_number").primaryKey().bindTo { it.pageNumber }
  val imageNumber = int("image_number").primaryKey().bindTo { it.imageNumber }
  val reservedAt = datetime("reserved_at").bindTo { it.reservedAt }
}

val Database.photo_reservations get() = this.sequenceOf(PhotoReservations)

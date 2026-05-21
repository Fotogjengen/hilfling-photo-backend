package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.date
import me.liuwj.ktorm.schema.uuid
import me.liuwj.ktorm.schema.varchar
import no.fg.hilflingbackend.dto.PositionDto
import no.fg.hilflingbackend.dto.PositionId
import no.fg.hilflingbackend.valueobject.Email
import java.util.UUID

interface Position : BaseModel<Position> {
  companion object : Entity.Factory<Position>()

  var title: String
  var email: String
}

object Positions : BaseTable<Position>("position") {
  val title = varchar("title").bindTo { it.title }
  val email = varchar("email").bindTo { it.email }
}

fun Position.toDto() =
  PositionDto(
    positionId = PositionId(this.id),
    title = this.title,
    email = Email(this.email),
  )

val Database.positions get() = this.sequenceOf(Positions)

object PhotoGangBangerToPositions : Table<Nothing>("photo_gang_banger_to_position") {
  val photoGangBangerId = uuid("photo_gang_banger_id")
  val positionId = uuid("position_id")
  val semesterStart = varchar("semester_start")
  val dateDeleted = date("date_deleted")
}

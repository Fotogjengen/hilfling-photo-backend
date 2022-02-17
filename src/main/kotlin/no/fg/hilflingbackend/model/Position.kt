package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.varchar
import no.fg.hilflingbackend.dto.PositionDto
import no.fg.hilflingbackend.dto.PositionId
import no.fg.hilflingbackend.value_object.Email

interface Position : BaseModel<Position> {
  companion object : Entity.Factory<Position>()

  var title: String
  var email: String
}

object Positions : BaseTable<Position>("position") {
  val title = varchar("title").bindTo { it.title }
  val email = varchar("email").bindTo { it.email }
}

fun Position.toDto() = PositionDto(
  positionId = PositionId(this.id),
  title = this.title,
  email = Email(this.email)
)

val Database.positions get() = this.sequenceOf(Positions)

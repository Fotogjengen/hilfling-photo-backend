package no.fg.hilflingbackend.model

import no.fg.hilflingbackend.dto.PositionDto
import no.fg.hilflingbackend.dto.PositionId
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.varchar

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
  email = this.email,
)

val Database.positions get() = this.sequenceOf(Positions)

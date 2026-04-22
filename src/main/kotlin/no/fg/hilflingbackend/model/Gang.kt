package no.fg.hilflingbackend.model

import no.fg.hilflingbackend.dto.GangDto
import no.fg.hilflingbackend.dto.GangId
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.varchar

interface Gang : BaseModel<Gang> {
  companion object : Entity.Factory<Gang>()

  var name: String
}

fun Gang.toDto() = GangDto(
  gangId = GangId(this.id),
  name = this.name
)

object Gangs : BaseTable<Gang>("gang") {
  val name = varchar("name").bindTo { it.name }
}

val Database.gangs get() = this.sequenceOf(Gangs)

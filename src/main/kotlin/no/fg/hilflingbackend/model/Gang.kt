package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.varchar
import no.fg.hilflingbackend.dto.GangDto
import no.fg.hilflingbackend.dto.GangId

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

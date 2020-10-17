package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.varchar

interface SecurityLevel : BaseModel<SecurityLevel> {
  companion object : Entity.Factory<SecurityLevel>()

  var type: String
}

object SecurityLevels : BaseTable<SecurityLevel>("security_level") {
  val type = varchar("type").bindTo { it.type }
}

val Database.security_levels get() = this.sequenceOf(SecurityLevels)

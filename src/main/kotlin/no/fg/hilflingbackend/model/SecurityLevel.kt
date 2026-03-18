package no.fg.hilflingbackend.model

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.varchar

interface SecurityLevel : BaseModel<SecurityLevel> {
  companion object : Entity.Factory<SecurityLevel>()

  var type: String
}

object SecurityLevels : BaseTable<SecurityLevel>("security_level") {
  val type = varchar("type").bindTo { it.type }
}

val Database.security_levels get() = this.sequenceOf(SecurityLevels)

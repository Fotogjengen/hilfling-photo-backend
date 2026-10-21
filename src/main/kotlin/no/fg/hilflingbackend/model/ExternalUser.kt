package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.boolean
import me.liuwj.ktorm.schema.varchar
import no.fg.hilflingbackend.dto.ExternalUserDto
import no.fg.hilflingbackend.dto.ExternalUserId
import no.fg.hilflingbackend.dto.SecurityLevelDto
import no.fg.hilflingbackend.valueobject.SecurityLevelType

interface ExternalUser : BaseModel<ExternalUser> {
  companion object : Entity.Factory<ExternalUser>()

  var username: String
  var password: String?
  var email: String?
  var fullName: String?
  var securityLevel: String?
  var description: String?
  var isActive: Boolean
}

fun ExternalUser.toDto(): ExternalUserDto =
  ExternalUserDto(
    externalUserId = ExternalUserId(this.id),
    username = this.username,
    password = this.password,
    email = this.email,
    fullName = this.fullName,
    securityLevel = this.securityLevel?.let { SecurityLevelDto(SecurityLevelType.valueOf(it)) },
    description = this.description,
    isActive = this.isActive,
  )

object ExternalUsers : BaseTable<ExternalUser>("external_user") {
  val username = varchar("username").bindTo { it.username }
  val password = varchar("password").bindTo { it.password }
  val email = varchar("email").bindTo { it.email }
  val fullName = varchar("full_name").bindTo { it.fullName }
  val securityLevel = varchar("security_level").bindTo { it.securityLevel }
  val description = varchar("description").bindTo { it.description }
  val isActive = boolean("is_active").bindTo { it.isActive }
}

val Database.external_users get() = this.sequenceOf(ExternalUsers)

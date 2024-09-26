package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.uuid
import me.liuwj.ktorm.schema.varchar
import no.fg.hilflingbackend.dto.SamfundetUserDto
import no.fg.hilflingbackend.dto.SamfundetUserId
import no.fg.hilflingbackend.dto.toDto
import no.fg.hilflingbackend.value_object.Email
import no.fg.hilflingbackend.value_object.PhoneNumber

interface SamfundetUser : BaseModel<SamfundetUser> {
  companion object : Entity.Factory<SamfundetUser>()

  var firstName: String
  var lastName: String
  var username: String

  // var email: Email
  var email: String
  var profilePicture: String
  var phoneNumber: String
  var sex: String

  var securityLevel: SecurityLevel
}

fun SamfundetUser.toDto() =
  SamfundetUserDto(
  samfundetUserId = SamfundetUserId(this.id),
  firstName = this.firstName,
  lastName = this.lastName,
  username = this.username,
  email = Email(this.email),
  profilePicturePath = this.profilePicture,
  phoneNumber = PhoneNumber(this.phoneNumber),
  securityLevel = this.securityLevel.toDto(),
  sex = this.sex,
  )

object SamfundetUsers : BaseTable<SamfundetUser>("samfundet_user") {
  val firstName = varchar("first_name").bindTo { it.firstName }
  val lastName = varchar("last_name").bindTo { it.lastName }
  val username = varchar("username").bindTo { it.username }

  // val email = varchar("email").bindTo { it.email.value  }
  val email = varchar("email").bindTo { it.email }
  val profilePicture = varchar("profile_picture").bindTo { it.profilePicture }
  val phoneNumber = varchar("phone_number").bindTo { it.phoneNumber }
  val sex = varchar("sex").bindTo { it.sex }

  val securityLevelId = uuid("security_level_id").references(SecurityLevels) { it.securityLevel }
}

val Database.samfundet_users
  get() = this.sequenceOf(SamfundetUsers)

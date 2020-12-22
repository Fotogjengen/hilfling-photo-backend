package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.SamfundetUser
import no.fg.hilflingbackend.model.SecurityLevel
import no.fg.hilflingbackend.value_object.Email
import no.fg.hilflingbackend.value_object.PhoneNumber
import java.util.UUID

data class SamfundetUserDto(
  val samfundetUserId: SamfundetUserId = SamfundetUserId(),
  val firstName: String,
  val lastName: String,
  val username: String,
  val phoneNumber: PhoneNumber,
  val email: Email,
  // TODO: Rename SQL-scheme and interface to match this variablename
  val profilePicturePath: String,
  val sex: String,
  // TODO: Do i have to make dto?
  val securituLevel: SecurityLevel
)

// Extend Dto object with a converter to ktorm interface
fun SamfundetUserDto.toEntity(): SamfundetUser {
  val dto = this
  return SamfundetUser {
    id = dto.samfundetUserId.id
    email = dto.email.value
    firstName = dto.firstName
    lastName = dto.lastName
    phoneNumber = dto.phoneNumber.value
    profilePicture = dto.profilePicturePath
  }
}

data class SamfundetUserId(
  override val id: UUID = UUID.randomUUID()
) : UuidId {
  override fun toString(): String = id.toString()
}

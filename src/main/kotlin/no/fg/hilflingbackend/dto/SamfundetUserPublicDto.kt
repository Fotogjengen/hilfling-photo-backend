package no.fg.hilflingbackend.dto

data class SamfundetUserPublicDto(
  val samfundetUserId: SamfundetUserId,
  val firstName: String,
  val lastName: String,
  val profilePicturePath: String,
)

fun SamfundetUserDto.toPublicDto() = SamfundetUserPublicDto(
  samfundetUserId = this.samfundetUserId,
  firstName = this.firstName,
  lastName = this.lastName,
  profilePicturePath = this.profilePicturePath,
)

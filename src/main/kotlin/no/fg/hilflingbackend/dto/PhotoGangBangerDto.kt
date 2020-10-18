package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.PhotoGangBanger
import no.fg.hilflingbackend.model.SamfundetUser
import java.util.*

data class PhotoGangBangerDto(
  val photoGangBangerId: PhotoGangBangerId = PhotoGangBangerId(),
  val relationShipStatus: RelationshipStatus,
  val semesterStart: SemesterStart,
  val isActive: Boolean,
  val isPang: Boolean,
  // TODO: Add value object
  val address: String,
  // TODO: Add value object
  val zipCode: String,
  val city: String,
  val samfundetUser: SamfundetUserDto,
)
fun PhotoGangBangerDto.toEntity(): PhotoGangBanger {
  val dto = this
  return PhotoGangBanger{
    id = dto.photoGangBangerId.id
    relationshipStatus = dto.relationShipStatus.status
    semesterStart = dto.semesterStart.value
    isPang = dto.isPang
    isActive = dto.isActive
    address = dto.address
    zipCode = dto.zipCode
    city = dto.city
    samfundetUser = SamfundetUser{
        id = dto.samfundetUser.samfundetUserId.id
        email = dto.samfundetUser.email.value
        firstName = dto.samfundetUser.firstName
        lastName = dto.samfundetUser.lastName
        phoneNumber = dto.samfundetUser.phoneNumber
        profilePicture = dto.samfundetUser.profilePicturePath
    }
  }
}

data class PhotoGangBangerId(
  override val id: UUID = UUID.randomUUID()
) : UuidId {
  override fun toString(): String = id.toString()
}

// TODO: Move to value objects
enum class RelationshipStatus(val status: String) {
  SINGLE("single"),
  MARRIED("married"),
  IN_A_RELATIONSHIP("relationship")
}

// TODO: Move to value objects
data class SemesterStart private constructor(val value: String) {
  companion object {
    // Overrides default constructor and adds a validator to it
    operator fun invoke(value: String): SemesterStart {
      // Validated
      return if (isValidSemesterStart(value))
      SemesterStart(value)
      else
      throw IllegalArgumentException(isValidSemesterStart(value).toString())
    }

    fun isValidSemesterStart(semesterStart: String): Boolean {
      // TODO: Implement
      return true
    }
    /*
    return Pattern.compile(
      "([HV])\d\w"
    ).matcher(semesterStart).matches()
  }
     */
  }
}

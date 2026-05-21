package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.PhotoGangBanger
import java.util.UUID

data class PhotoGangBangerPatchRequestDto(
  val photoGangBangerId: PhotoGangBangerId,
  val relationshipStatus: RelationshipStatus?,
  val semesterStart: SemesterStart?,
  val isActive: Boolean?,
  val isPang: Boolean?,
  val firstName: String?,
  val lastName: String?,
  val username: String?,
  val email: String?,
  val profilePicture: String?,
  val phoneNumber: String?,
)

data class PhotoGangBangerDto(
  val photoGangBangerId: PhotoGangBangerId = PhotoGangBangerId(),
  val relationShipStatus: RelationshipStatus,
  val semesterStart: SemesterStart,
  val isActive: Boolean,
  var isPang: Boolean,
  val firstName: String,
  val lastName: String,
  val username: String,
  val email: String,
  val profilePicture: String,
  val phoneNumber: String,
)

fun PhotoGangBangerDto.toEntity(): PhotoGangBanger {
  val dto = this
  return PhotoGangBanger {
    id = dto.photoGangBangerId.id
    relationshipStatus = dto.relationShipStatus.status
    semesterStart = dto.semesterStart.value
    isPang = dto.isPang
    isActive = dto.isActive
    firstName = dto.firstName
    lastName = dto.lastName
    username = dto.username
    email = dto.email
    profilePicture = dto.profilePicture
    phoneNumber = dto.phoneNumber
  }
}

data class PhotoGangBangerId(
  override val id: UUID = UUID.randomUUID()
) : UuidId {
  override fun toString(): String = id.toString()
}

// TODO: Move to value objects
enum class RelationshipStatus(val status: String) {
  single("single"),
  relationship("relationship"),
  married("married")
}

// TODO: Move to value objects
data class SemesterStart private constructor(val value: String) {
  companion object {
    operator fun invoke(value: String): SemesterStart {
      return if (isValidSemesterStart(value)) {
        SemesterStart(value)
      } else {
        throw IllegalArgumentException(isValidSemesterStart(value).toString())
      }
    }

    fun isValidSemesterStart(semesterStart: String): Boolean {
      // TODO: Implement
      return true
    }
  }
}

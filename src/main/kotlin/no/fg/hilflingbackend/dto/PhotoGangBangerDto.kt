package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.PhotoGangBanger
import java.util.UUID

data class PhotoGangBangerPatchRequestDto(
  val photoGangBangerId: PhotoGangBangerId,
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
  val semesterStart: SemesterStart,
  val isActive: Boolean,
  val isPang: Boolean,
  val firstName: String,
  val lastName: String,
  val username: String,
  val email: String,
  val profilePicture: String,
  val phoneNumber: String,
  val positions: List<PositionDto> = emptyList(),
)

fun PhotoGangBangerDto.toEntity(): PhotoGangBanger =
  PhotoGangBanger {
    id = photoGangBangerId.id
    semesterStart = this@toEntity.semesterStart.value
    isPang = this@toEntity.isPang
    isActive = this@toEntity.isActive
    firstName = this@toEntity.firstName
    lastName = this@toEntity.lastName
    username = this@toEntity.username
    email = this@toEntity.email
    profilePicture = this@toEntity.profilePicture
    phoneNumber = this@toEntity.phoneNumber
  }

data class PhotoGangBangerId(
  override val id: UUID = UUID.randomUUID(),
) : UuidId {
  override fun toString(): String = id.toString()
}

// TODO: Move to value objects
data class SemesterStart private constructor(
  val value: String,
) {
  companion object {
    operator fun invoke(value: String): SemesterStart =
      if (isValidSemesterStart(value)) {
        SemesterStart(value)
      } else {
        throw IllegalArgumentException("Invalid semester start value: '$value'")
      }

    fun isValidSemesterStart(semesterStart: String): Boolean {
      // TODO: Implement
      return true
    }
  }
}

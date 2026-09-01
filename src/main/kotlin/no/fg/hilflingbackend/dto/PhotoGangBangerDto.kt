package no.fg.hilflingbackend.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import no.fg.hilflingbackend.model.PhotoGangBanger
import no.fg.hilflingbackend.valueobject.SemesterStart
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
  val positions: List<MemberPositionDto> = emptyList(),
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

class PhotoGangBangerId @JsonCreator constructor(
  @JsonProperty("id") id: String? = null,
) : UuidId {
  override val id: UUID =
    id
      ?.takeIf { it.isNotBlank() }
      ?.let(UUID::fromString)
      ?: UUID.randomUUID()

  constructor(id: UUID) : this(id.toString())

  override fun equals(other: Any?): Boolean =
    this === other || (other is PhotoGangBangerId && id == other.id)

  override fun hashCode(): Int = id.hashCode()

  override fun toString(): String = id.toString()
}

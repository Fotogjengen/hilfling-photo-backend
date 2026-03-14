package no.fg.hilflingbackend.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import no.fg.hilflingbackend.model.PhotoGangBanger
import java.util.UUID

data class PhotoGangBangerPatchRequestDto(
  val photoGangBangerId: PhotoGangBangerId,
  val semesterStart: SemesterStart?,
  val isActive: Boolean?,
  val isPang: Boolean?,
  val address: String?,
  val zipCode: String?,
  val city: String?,
  val samfundetUser: SamfundetUserPatchRequestDto?,
  val position: PositionDto?
)

data class PhotoGangBangerDto(
  val photoGangBangerId: PhotoGangBangerId = PhotoGangBangerId(),
  val semesterStart: SemesterStart,
  val isActive: Boolean,
  var isPang: Boolean,
  // TODO: Add value object
  val address: String,
  // TODO: Add value object
  val zipCode: String,
  val city: String,
  val samfundetUser: SamfundetUserDto,
  val position: PositionDto
)

fun PhotoGangBangerDto.toEntity(): PhotoGangBanger {
  val dto = this
  return PhotoGangBanger {
    id = dto.photoGangBangerId.id
    semesterStart = dto.semesterStart.value
    isPang = dto.isPang
    isActive = dto.isActive
    address = dto.address
    zipCode = dto.zipCode
    city = dto.city
    position = dto.position.toEntity()
    samfundetUser = dto.samfundetUser.toEntity()
  }
}

data class PhotoGangBangerId(
  override val id: UUID = UUID.randomUUID()
) : UuidId {
  override fun toString(): String = id.toString()
}

// TODO: Move to value objects
data class SemesterStart(val value: String) {
  init {
    require(isValidSemesterStart(value)) { "Invalid semesterStart: $value" }
  }

  @JsonValue
  fun toJson(): String = value

  companion object {
    @JvmStatic
    @JsonCreator
    fun from(value: String): SemesterStart = SemesterStart(value)

    operator fun invoke(value: String): SemesterStart = SemesterStart(value)

    fun isValidSemesterStart(semesterStart: String): Boolean {
      // TODO: kan lage semesterstart check
      return true
    }
  }
}

package no.fg.hilflingbackend.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import no.fg.hilflingbackend.model.PhotoGangBanger
import java.util.UUID

data class PhotoGangBangerPatchRequestDto(
  val photoGangBangerId: PhotoGangBangerId,
  val semesterStart: String?,
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
  val semesterStart: String,
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
    semesterStart = dto.semesterStart
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

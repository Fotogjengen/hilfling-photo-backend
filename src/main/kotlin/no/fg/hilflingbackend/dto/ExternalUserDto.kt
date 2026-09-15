package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.ExternalUser
import java.util.UUID

data class ExternalUserPatchRequestDto(
  val externalUserId: ExternalUserId,
  val username: String?,
  val password: String?,
  val email: String?,
  val fullName: String?,
  val securityLevel: SecurityLevelDto?,
  val description: String?,
  val isActive: Boolean?,
)

data class ExternalUserDto(
  val externalUserId: ExternalUserId = ExternalUserId(),
  val username: String,
  val password: String?,
  val email: String?,
  val fullName: String?,
  val securityLevel: SecurityLevelDto?,
  val description: String?,
  val isActive: Boolean,
)

data class ExternalUserId(
  override val id: UUID = UUID.randomUUID(),
) : UuidId {
  override fun toString(): String = id.toString()
}

fun ExternalUserDto.toEntity(): ExternalUser {
  val dto = this
  return ExternalUser {
    id = dto.externalUserId.id
    username = dto.username
    password = dto.password
    email = dto.email
    fullName = dto.fullName
    securityLevel = dto.securityLevel?.securityLevelType?.type
    description = dto.description
    isActive = dto.isActive
  }
}

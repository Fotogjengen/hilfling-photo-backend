package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.valueobject.Permission
import no.fg.hilflingbackend.valueobject.SecurityLevelType

data class JwtTokenPayload(
  val username: String,
  val positionId: String?,
  val securityLevel: SecurityLevelType,
  val permissions: List<Permission> = emptyList(),
)

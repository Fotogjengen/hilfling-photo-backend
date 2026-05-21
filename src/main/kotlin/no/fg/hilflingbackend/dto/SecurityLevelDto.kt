package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.SecurityLevel
import no.fg.hilflingbackend.valueobject.SecurityLevelType

data class SecurityLevelDto(
  val securityLevelType: SecurityLevelType,
)

fun SecurityLevelDto.toEntity(): SecurityLevel = SecurityLevel(securityLevelType)

fun SecurityLevel.toDto(): SecurityLevelDto = SecurityLevelDto(type)

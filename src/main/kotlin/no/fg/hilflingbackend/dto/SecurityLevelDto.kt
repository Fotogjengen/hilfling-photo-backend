package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.SecurityLevel
import no.fg.hilflingbackend.value_object.SecurityLevelType

data class SecurityLevelDto(val securityLevelType: SecurityLevelType)

fun SecurityLevelDto.toEntity(): SecurityLevel = SecurityLevel(securityLevelType)

fun SecurityLevel.toDto(): SecurityLevelDto = SecurityLevelDto(type)

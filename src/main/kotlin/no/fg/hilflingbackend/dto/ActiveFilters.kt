package no.fg.hilflingbackend.dto

import java.time.LocalDate
import java.util.UUID

data class ActiveFilters(
  val placeIds: List<UUID> = emptyList(),
  val categoryIds: List<UUID> = emptyList(),
  val eventOwnerIds: List<UUID> = emptyList(),
  val albumIds: List<UUID> = emptyList(),
  val securityLevels: List<String> = emptyList(),
  val dateFrom: LocalDate? = null,
  val dateTo: LocalDate? = null,
)

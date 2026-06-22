package no.fg.hilflingbackend.dto

import java.time.LocalDate
import java.util.UUID

data class EventCardDto(
  val motiveId: UUID?,
  val motiveTitle: String?,
  val date_created: LocalDate?,
  val frontPageSmallPhotoUrl: String?,
  val eventOwnerName: String?,
  val dateDeleted:LocalDate?,
)

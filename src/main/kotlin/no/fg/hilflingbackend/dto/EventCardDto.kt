package no.fg.hilflingbackend.dto

import java.time.LocalDate
import java.util.UUID

data class EventCardDto(
  // TODO: Need dto?
  val motiveId: UUID?,
  val motiveTitle: String?,
  val date_created: LocalDate?,
  // TODO: Need dto?
  // val locationTaken: String?,
  // val albumType: String?,
  val frontPageSmallPhotoUrl: String?,
  val eventOwnerName: String?,
)

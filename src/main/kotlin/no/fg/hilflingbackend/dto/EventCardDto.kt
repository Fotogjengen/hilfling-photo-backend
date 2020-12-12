package no.fg.hilflingbackend.dto

import java.time.LocalDate

data class EventCardDto(
  // TODO: Need dto?
  val motiveTitle: String?,
  val date_crated: LocalDate?,
  // TODO: Need dto?
  val locationTaken: String?,
  //val albumType: String?,
  val frontPageSmallPhotoUrl: String?,
  val eventOwnerName: String?,
)

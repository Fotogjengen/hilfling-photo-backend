package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.valueobject.SemesterStart

data class PhotoGangBangerPositionPatchRequestDto(
  val photoGangBangerId: PhotoGangBangerId,
  val semesterStart: SemesterStart,
  val position: PositionDto?,
  val semesterEnd: SemesterStart?,
)

data class PhotoGangBangerPositionDto(
  val photoGangBangerId: PhotoGangBangerId,
  val semesterStart: SemesterStart,
  val photoGangBangerDto: PhotoGangBangerDto,
  val position: PositionDto,
  val semesterEnd: SemesterStart?,
)

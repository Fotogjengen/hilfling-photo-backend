package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.valueobject.SemesterStart

data class PhotoGangBangerPositionRequestDto(
  val positionId: PositionId,
  val semesterStart: SemesterStart,
  val semesterEnd: SemesterStart? = null,
)

data class PhotoGangBangerPositionsPutRequestDto(
  val photoGangBangerId: PhotoGangBangerId,
  val positions: List<PhotoGangBangerPositionRequestDto>,
)

data class PhotoGangBangerPositionDto(
  val photoGangBangerId: PhotoGangBangerId,
  val semesterStart: SemesterStart,
  val photoGangBangerDto: PhotoGangBangerDto,
  val position: PositionDto,
  val semesterEnd: SemesterStart?,
)

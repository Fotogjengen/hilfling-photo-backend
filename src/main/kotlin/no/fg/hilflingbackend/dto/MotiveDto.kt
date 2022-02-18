package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.Motive
import java.time.LocalDate
import java.util.UUID

data class MotivePatchRequestDto(
  val motiveId: MotiveId,
  val title: String?,
  val categoryDto: CategoryDto?,
  val eventOwnerDto: EventOwnerDto?,
  val albumDto: AlbumDto?,
)

data class MotiveDto(
  val motiveId: MotiveId = MotiveId(),
  val title: String,
  val categoryDto: CategoryDto,
  val eventOwnerDto: EventOwnerDto,
  val albumDto: AlbumDto,
  val dateCreated: LocalDate?
) {
  constructor(
    motiveId: MotiveId = MotiveId(),
    title: String,
    categoryDto: CategoryDto,
    eventOwnerDto: EventOwnerDto,
    albumDto: AlbumDto
  ) : this(motiveId, title, categoryDto, eventOwnerDto, albumDto, null)
}

data class MotiveId(
  override val id: UUID = UUID.randomUUID()
) : UuidId {
  override fun toString(): String = id.toString()
}

fun MotiveDto.toEntity(): Motive {
  val dto = this
  return Motive {
    id = dto.motiveId.id
    title = dto.title
    category = dto.categoryDto.toEntity()
    eventOwner = dto.eventOwnerDto.toEntity()
    album = dto.albumDto.toEntity()
  }
}

package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.Motive
import java.time.LocalDate
import java.util.UUID

data class MotiveCreateRequestDto(
  val title: String,
  val date: LocalDate,
  val categoryDto: CategoryDto,
  val eventOwnerDto: EventOwnerDto,
  val placeDto: PlaceDto,
  val securityLevel: SecurityLevelDto,
  val albumDto: AlbumDto?,
  val analogAlbumDto: AlbumDto?,
)

data class MotivePatchRequestDto(
  val motiveId: MotiveId,
  val title: String?,
  val date: LocalDate?,
  val categoryDto: CategoryDto?,
  val eventOwnerDto: EventOwnerDto?,
  val placeDto: PlaceDto?,
  val securityLevel: SecurityLevelDto?,
  val albumDto: AlbumDto?,
  val analogAlbumDto: AlbumDto?,
)

data class MotiveDto(
  val motiveId: MotiveId = MotiveId(),
  val title: String,
  val date: LocalDate,
  val categoryDto: CategoryDto,
  val eventOwnerDto: EventOwnerDto,
  val placeDto: PlaceDto,
  val securityLevel: SecurityLevelDto,
  val albumDto: AlbumDto?,
  val analogAlbumDto: AlbumDto?,
  val dateCreated: LocalDate?,
)

data class MotiveDefaultsDto(
  val eventOwnerDto: EventOwnerDto,
  val securityLevel: SecurityLevelDto,
  val albumDto: AlbumDto?,
  val analogAlbumDto: AlbumDto?,
  val dateCreated: LocalDate,
  val date: LocalDate,
)

data class MotiveId(
  override val id: UUID = UUID.randomUUID(),
) : UuidId {
  override fun toString(): String = id.toString()
}

fun MotiveDto.toEntity(): Motive {
  val dto = this
  return Motive {
    id = dto.motiveId.id
    title = dto.title
    date = dto.date
    category = dto.categoryDto.toEntity()
    eventOwner = dto.eventOwnerDto.toEntity()
    place = dto.placeDto.toEntity()
    securityLevel = dto.securityLevel.securityLevelType.type
    album = dto.albumDto?.toEntity()
    analogAlbum = dto.analogAlbumDto?.toEntity()
    dateCreated = dto.dateCreated!!
  }
}

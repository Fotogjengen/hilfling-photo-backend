package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.Motive
import java.util.UUID

data class SearchDto(
  val motiveId: MotiveId = MotiveId(),
  val title: String,
  val categoryDto: CategoryDto,
  val eventOwnerDto: EventOwnerDto,
  val albumDto: AlbumDto
)

data class SearchTerm(
  override val id: UUID = UUID.randomUUID()
) : UuidId {
  override fun toString(): String = id.toString()
}

fun SearchDto.toEntity(): Motive {
  val dto = this
  return Motive {
    id = dto.motiveId.id
    title = dto.title
    category = dto.categoryDto.toEntity()
    eventOwner = dto.eventOwnerDto.toEntity()
    album = dto.albumDto.toEntity()
  }
}

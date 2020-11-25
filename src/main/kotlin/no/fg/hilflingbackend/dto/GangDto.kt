package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.Gang
import java.util.*

data class GangDto(
  val gangId: GangId = GangId(),
  val name: String?
)

data class GangId(
  override val id: UUID = UUID.randomUUID()
) : UuidId {
  override fun toString(): String = id.toString()
}

fun GangDto.toEntity(): Gang {
  val dto = this
  return Gang{
    id = dto.gangId.id
    name = dto.name
  }
}

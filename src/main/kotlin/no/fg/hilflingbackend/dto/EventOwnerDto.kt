package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.EventOwner
import java.util.*

data class EventOwnerDto(
  val eventOwnerId: EventOwnerId = EventOwnerId(),
  val name: EventOwnerName,
) {
  // Constructor for Entity
  constructor(eventOwner: EventOwner) : this(
    EventOwnerId(eventOwner.id), EventOwnerName.valueOf(eventOwner.name)
  )
}
// TODO: Move to value object?
enum class EventOwnerName(val eventOwnerName: String) {
  ISFIT("ISFIT"),
  UKA("UKA"),
  Samfundet("Samfundet")
}

data class EventOwnerId(
  override val id: UUID = UUID.randomUUID()
) : UuidId {
  override fun toString(): String = id.toString()
}

fun EventOwnerDto.toEntity(): EventOwner {
  val dto = this
  return EventOwner {
    id = dto.eventOwnerId.id
    name = dto.name.eventOwnerName
  }
}

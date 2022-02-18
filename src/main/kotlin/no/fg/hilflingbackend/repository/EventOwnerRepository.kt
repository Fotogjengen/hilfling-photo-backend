package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.dto.EventOwnerDto
import no.fg.hilflingbackend.dto.EventOwnerId
import no.fg.hilflingbackend.dto.EventOwnerName
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.EventOwner
import no.fg.hilflingbackend.model.EventOwners
import no.fg.hilflingbackend.model.event_owners
import org.springframework.stereotype.Repository

@Repository
open class EventOwnerRepository(database: Database) : BaseRepository<EventOwner, EventOwnerDto>(table = EventOwners, database = database) {
  override fun convertToClass(qrs: QueryRowSet): EventOwnerDto = EventOwnerDto(
    eventOwnerId = EventOwnerId(qrs[EventOwners.id]!!),
    name = EventOwnerName.valueOf(qrs[EventOwners.name]!!)
  )

  override fun create(dto: EventOwnerDto): Int {
    return database.event_owners.add(dto.toEntity())
  }

  fun patch(dto: EventOwnerDto): Int {
    return database.event_owners.update(dto.toEntity())
  }

  @Deprecated("Will be removed when MockDataService is updated. Use findByEventOwnerName instead.")
  fun findByType(eventOwnerName: EventOwnerName): EventOwnerDto? =
    database.event_owners.find { it.name eq eventOwnerName.eventOwnerName }?.let {
      EventOwnerDto(it)
    }

  fun findByEventOwnerName(name: EventOwnerName): EventOwnerDto? =
    database.event_owners.find { it.name eq name.eventOwnerName }?.let {
      EventOwnerDto(it)
    }
}

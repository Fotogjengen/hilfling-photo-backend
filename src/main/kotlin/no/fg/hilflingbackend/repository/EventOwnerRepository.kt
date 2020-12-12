package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.dto.EventOwnerDto
import no.fg.hilflingbackend.dto.EventOwnerName
import no.fg.hilflingbackend.model.EventOwner
import no.fg.hilflingbackend.model.event_owners
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
open class EventOwnerRepository {
  @Autowired
  open lateinit var database: Database

  fun findById(id: UUID): EventOwner? {
    return database.event_owners.find { it.id eq id }
  }
  fun findByType(eventOwnerName: EventOwnerName): EventOwnerDto? =
    database
      .event_owners
      .find { it.name eq eventOwnerName.eventOwnerName }?.let {
        EventOwnerDto(it)
      }


  fun findAll(): List<EventOwner> {
    return database.event_owners.toList()
  }

  fun create(
    eventOwner: EventOwner
  ): Int{
    return database.event_owners.add(eventOwner)
  }
}

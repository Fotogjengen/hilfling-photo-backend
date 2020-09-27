package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.model.EventOwner
import no.fg.hilflingbackend.model.event_owners
import org.springframework.beans.factory.annotation.Autowired

class EventOwnerRepository {
    @Autowired
    lateinit var database: Database

    fun findById(id: Int): EventOwner? {
        return database.event_owners.find { it.id eq id }
    }

    fun findAll(): List<EventOwner> {
        return database.event_owners.toList()
    }

    fun create(
            eventOwner: EventOwner
    ): EventOwner {
        val eventOwnerFromDatabase = EventOwner{
            this.name = eventOwner.name
        }
        database.event_owners.add(eventOwnerFromDatabase)
        return eventOwnerFromDatabase
    }
}
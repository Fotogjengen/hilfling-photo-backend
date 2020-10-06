package no.fg.hilflingbackend.model;

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.*

interface EventOwner : BaseModel<EventOwner> {
    companion object : Entity.Factory<EventOwner>()

    var name: String
}

object EventOwners : BaseTable<EventOwner>("event_owner") {
    val name = varchar("name").bindTo { it.name }
}

val Database.event_owners get() = this.sequenceOf(EventOwners)
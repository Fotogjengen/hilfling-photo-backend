package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.*

interface Place : BaseModel<Place> {
    companion object : Entity.Factory<Place>()

    var name: String
}

object Places : BaseTable<Place>("place") {
    val location = varchar("location").bindTo { it.name }
}

val Database.places get() = this.sequenceOf(Places)
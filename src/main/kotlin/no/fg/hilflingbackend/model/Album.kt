package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.*

interface Album : BaseModel<Album> {
    companion object : Entity.Factory<Album>()
    var title: String
    var isAnalog : Boolean
}

object Albums : BaseTable<Album>("album") {
    val title = varchar("title").bindTo { it.title }
    val isAnalog = boolean("is_analog").bindTo { it.isAnalog }
}

val Database.albums get() = this.sequenceOf(Albums)
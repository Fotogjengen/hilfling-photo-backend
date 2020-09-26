package hilfling.backend.hilfing.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.*
import java.time.LocalDate

interface Album : Entity<Album> {
    companion object : Entity.Factory<Album>()

    val id : Long
    var title: String
    var timeCreated : LocalDate
    var isAnalog : Boolean
}

object Albums : Table<Album>("t_album") {
    val id = long("id").primaryKey().bindTo { it.id }
    val title = varchar("title").bindTo { it.title }
    val timeCreated = date("time_created").bindTo { it.timeCreated }
    val isAnalog = boolean("is_analog").bindTo { it.isAnalog }
}

val Database.albums get() = this.sequenceOf(Albums)
package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.*
import org.jetbrains.annotations.NotNull

interface Position : BaseModel<Position> {
    companion object : Entity.Factory<Position>()
    var title: String
    var email : String
}

object Positions : BaseTable<Position>("position") {
    val title = varchar("title").bindTo { it.title }
    val email = varchar("email").bindTo { it.email }
}

val Database.positions get() = this.sequenceOf(Positions)
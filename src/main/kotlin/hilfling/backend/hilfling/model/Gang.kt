package hilfling.backend.hilfing.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.*

interface Gang : BaseModel<Gang> {
    companion object : Entity.Factory<Gang>()

    var name: String
}

object Gangs : BaseTable<Gang>("gang") {
    val name = varchar("name").bindTo { it.name }
}

val Database.gangs get() = this.sequenceOf(Gangs)
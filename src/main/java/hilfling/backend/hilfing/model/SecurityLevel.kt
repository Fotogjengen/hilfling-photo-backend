package hilfling.backend.hilfing.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.*
import java.time.LocalDate

interface SecurityLevel : Entity<SecurityLevel> {
    companion object : Entity.Factory<SecurityLevel>()

    val id: Long
    var type: String
    var dateCreated: LocalDate
}

object SecurityLevels : Table<SecurityLevel>("t_article") {
    val id = long("id").primaryKey().bindTo { it.id }
    val type = varchar("type").bindTo { it.type }
    val dateCreated = date("date_created").bindTo { it.dateCreated }
}

val Database.security_levels get() = this.sequenceOf(SecurityLevels)
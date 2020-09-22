package hilfling.backend.hilfing.model

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int


interface Gang : Entity<Gang> {
    companion object : Entity.Factory<Gang>()
    val id : Int
    val name: String
}

object Gans : Table<Gang>("gangs") {
    val id = int("id").primaryKey().bindTo {it.id}

}
package no.fg.hilflingbackend.model

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.date
import me.liuwj.ktorm.schema.uuid
import java.time.LocalDate
import java.util.*

open interface BaseModel<E: Entity<E>>: Entity<E> {
    val id: UUID
    var dateCreated: LocalDate
}

open class BaseTable<E: BaseModel<E>>(tableName: String) : Table<E>(tableName){
    val id = uuid("id").primaryKey().bindTo { it.id }
    val dateCreated = date("date_created").bindTo{it.dateCreated}

}

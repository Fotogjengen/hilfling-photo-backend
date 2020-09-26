package hilfling.backend.hilfing.model

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.date
import me.liuwj.ktorm.schema.long
import java.time.LocalDate

open interface BaseModel<T: Entity<T>>: Entity<T> {
    //companion object : Entity.Factory<T>()
    val id: Long
    var dateCreated: LocalDate
}

open class BaseTable<T: BaseModel<T>>(tableName: String) : Table<T>(tableName){
    val id = long("id").primaryKey().bindTo { it.id }
    val dateCreated = date("date_created").bindTo{it.dateCreated}

}
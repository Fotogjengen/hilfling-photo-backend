package no.fg.hilflingbackend.model

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.uuid
import java.time.LocalDate
import java.util.UUID

open interface BaseModel<E : Entity<E>> : Entity<E> {
  var id: UUID
  var dateCreated: LocalDate
  var dateDeleted: LocalDate
}

open class BaseTable<E : BaseModel<E>>(tableName: String) : Table<E>(tableName) {
  val id = uuid("id").primaryKey().bindTo { it.id }
  val dateCreated = date("date_created").bindTo { it.dateCreated }
  val dateDeleted = date("date_deleted").bindTo { it.dateDeleted }
}

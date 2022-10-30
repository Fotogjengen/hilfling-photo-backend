package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.boolean
import me.liuwj.ktorm.schema.date
import me.liuwj.ktorm.schema.varchar
import java.time.LocalDate

interface PhotographyRequest : BaseModel<PhotographyRequest> {
  companion object : Entity.Factory<PhotographyRequest>()

  // TODO: Make LocalDateTime
  var startTime: LocalDate

  // TODO: Make LocalDateTime
  var endTime: LocalDate
  var place: String
  var isIntern: Boolean
  var type: String
  var name: String
  var email: String
  var phone: String
  var description: String
}

object PhotographyRequests : BaseTable<PhotographyRequest>("photography_request") {
  val startTime = date("start_time").bindTo { it.startTime }
  val endTime = date("end_time").bindTo { it.endTime }
  val place = varchar("place").bindTo { it.place }
  val isIntern = boolean("is_intern").bindTo { it.isIntern }
  val type = varchar("type").bindTo { it.type }
  val name = varchar("name").bindTo { it.name }
  val email = varchar("email").bindTo { it.email }
  val phone = varchar("phone").bindTo { it.phone }
  val description = varchar("description").bindTo { it.description }
}

val Database.photography_requests get() = this.sequenceOf(PhotographyRequests)

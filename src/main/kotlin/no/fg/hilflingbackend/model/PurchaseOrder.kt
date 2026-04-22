package no.fg.hilflingbackend.model

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.boolean
import org.ktorm.schema.varchar

interface PurchaseOrder : BaseModel<PurchaseOrder> {
  companion object : Entity.Factory<PurchaseOrder>()

  var name: String
  var email: String
  var address: String
  var zipCode: String
  var city: String
  var sendByPost: Boolean
  var comment: String
  var isCompleted: Boolean
}

object PurchaseOrders : BaseTable<PurchaseOrder>("purchase_order") {
  val name = varchar("name").bindTo { it.name }
  val email = varchar("email").bindTo { it.email }
  val address = varchar("address").bindTo { it.address }
  val zipCode = varchar("zip_code").bindTo { it.zipCode }
  val city = varchar("city").bindTo { it.city }
  val sendByPost = boolean("send_by_post").bindTo { it.sendByPost }
  val comment = varchar("comment").bindTo { it.comment }
  val isCompleted = boolean("is_completed").bindTo { it.isCompleted }
}

val Database.purchase_orders get() = this.sequenceOf(PurchaseOrders)

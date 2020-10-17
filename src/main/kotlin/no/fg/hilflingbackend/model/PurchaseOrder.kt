package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.boolean
import me.liuwj.ktorm.schema.varchar

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

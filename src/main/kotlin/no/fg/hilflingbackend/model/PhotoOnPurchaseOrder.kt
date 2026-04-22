package no.fg.hilflingbackend.model

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.int
import org.ktorm.schema.varchar

interface PhotoOnPurchaseOrder : BaseModel<PhotoOnPurchaseOrder> {
  companion object : Entity.Factory<PhotoOnPurchaseOrder>()

  var size: String
  var amount: Int

  // Foreign keys
  var purchaseOrder: PurchaseOrder
  var photo: Photo
}

object PhotoOnPurchaseOrders : BaseTable<PhotoOnPurchaseOrder>("photo_on_purchase_order") {
  val size = varchar("size").bindTo { it.size }
  val amount = int("amount").bindTo { it.amount }

  // Foreign keys
  val purchaseOrderId = int("purchase_order_id").references(PurchaseOrders) { it.purchaseOrder }
  val photoId = int("photo_id").references(Photos) { it.photo }
}

val Database.photo_on_purchase_orders get() = this.sequenceOf(PhotoOnPurchaseOrders)

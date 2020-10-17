package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.model.PurchaseOrder
import no.fg.hilflingbackend.model.purchase_orders
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
open class PurchaseOrderRepository {
  @Autowired
  open lateinit var database: Database

  fun findById(id: UUID): PurchaseOrder? {
    return database.purchase_orders.find { it.id eq id }
  }

  fun findAll(): List<PurchaseOrder> {
    return database.purchase_orders.toList()
  }

  fun create(
    purchaseOrder: PurchaseOrder
  ): PurchaseOrder {
    val purchaseOrderFromDatabase = PurchaseOrder {
      this.name = purchaseOrder.name
      this.email = purchaseOrder.email
      this.address = purchaseOrder.address
      this.zipCode = purchaseOrder.zipCode
      this.city = purchaseOrder.city
      this.sendByPost = purchaseOrder.sendByPost
      this.comment = purchaseOrder.comment
      this.isCompleted = purchaseOrder.isCompleted
    }
    database.purchase_orders.add(purchaseOrderFromDatabase)
    return purchaseOrderFromDatabase
  }
  // TODO: DELETE
}

package hilfling.backend.hilfling.repository

import hilfling.backend.hilfling.model.PurchaseOrder
import hilfling.backend.hilfling.model.purchase_orders
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import org.springframework.beans.factory.annotation.Autowired

class PurchaseOrderRepository {
    @Autowired
    lateinit var database: Database

    fun findById(id: Long): PurchaseOrder? {
        return database.purchase_orders.find{it.id eq  id}
    }

    fun findAll(): List<PurchaseOrder> {
        return database.purchase_orders.toList()
    }

    fun create(
            purchaseOrder: PurchaseOrder
    ): PurchaseOrder {
        val purchaseOrder = PurchaseOrder{
            this.name = purchaseOrder.name;
            this.email = purchaseOrder.email;
            this.address = purchaseOrder.address;
            this.zipCode = purchaseOrder.zipCode;
            this.city = purchaseOrder.city;
            this.sendByPost = purchaseOrder.sendByPost;
            this.comment = purchaseOrder.comment;
            this.isCompleted = purchaseOrder.isCompleted;
        }
        database.purchase_orders.add(purchaseOrder)
        return purchaseOrder
    }
    // TODO: DELETE
}
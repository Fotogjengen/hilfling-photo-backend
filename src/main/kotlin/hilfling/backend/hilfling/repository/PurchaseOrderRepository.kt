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

    fun createPurchaseOrder(
            name: String,
            email: String,
            address: String,
            zipCode: String,
            city: String,
            sendByPost: Boolean,
            comment: String,
            isCompleted: Boolean = false
    ): PurchaseOrder {
        val purchaseOrder = PurchaseOrder{
            this.name = name;
            this.email = email;
            this.address = address;
            this.zipCode = zipCode;
            this.city = city;
            this.sendByPost = sendByPost;
            this.comment = comment;
            this.isCompleted = isCompleted;
        }
        database.purchase_orders.add(purchaseOrder)
        return purchaseOrder
    }
    // TODO: DELETE
}
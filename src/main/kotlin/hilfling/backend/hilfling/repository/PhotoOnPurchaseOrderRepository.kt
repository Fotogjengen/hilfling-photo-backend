package hilfling.backend.hilfling.repository

import hilfling.backend.hilfing.model.*
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.from
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.where
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate

class PhotoOnPurchaseOrderRepository {
    @Autowired
    lateinit var database: Database

    fun findById(id: Long): PhotoOnPurchaseOrder? {
        return database.photo_on_purchase_orders.find { it.id eq id }
    }

    fun findAll(): List<PhotoOnPurchaseOrder> {
        return database.photo_on_purchase_orders.toList()
    }

    fun create(
            size: String,
            amount: Int,
            purchaseOrder: PurchaseOrder,
            photo: Photo
    ): PhotoOnPurchaseOrder {
        val photoOnPurchaseOrder = PhotoOnPurchaseOrder{
            this.size = size
            this.amount = amount
            this.purchaseOrder = purchaseOrder
            this.photo = photo
        }
        database.photo_on_purchase_orders.add(photoOnPurchaseOrder)
        return photoOnPurchaseOrder
    }
}
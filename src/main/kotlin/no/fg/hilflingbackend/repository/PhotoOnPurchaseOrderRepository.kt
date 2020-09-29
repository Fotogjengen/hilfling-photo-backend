package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.model.Photo
import no.fg.hilflingbackend.model.PhotoOnPurchaseOrder
import no.fg.hilflingbackend.model.PurchaseOrder
import no.fg.hilflingbackend.model.photo_on_purchase_orders
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class PhotoOnPurchaseOrderRepository {
    @Autowired
    open lateinit var database: Database

    fun findById(id: Int): PhotoOnPurchaseOrder? {
        return database.photo_on_purchase_orders.find { it.id eq id }
    }

    fun findAll(): List<PhotoOnPurchaseOrder> {
        return database.photo_on_purchase_orders.toList()
    }

    fun create(
            photoOnPurchaseOrder: PhotoOnPurchaseOrder
    ): PhotoOnPurchaseOrder {
        val photoOnPurchaseOrderFromDatabase = PhotoOnPurchaseOrder{
            this.size = photoOnPurchaseOrder.size
            this.amount = photoOnPurchaseOrder.amount
            this.purchaseOrder = photoOnPurchaseOrder.purchaseOrder
            this.photo = photoOnPurchaseOrder.photo
        }
        database.photo_on_purchase_orders.add(photoOnPurchaseOrderFromDatabase)
        return photoOnPurchaseOrderFromDatabase
    }
}
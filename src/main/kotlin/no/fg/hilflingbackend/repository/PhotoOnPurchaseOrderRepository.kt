package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.model.PhotoOnPurchaseOrder
import no.fg.hilflingbackend.model.photo_on_purchase_orders
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
open class PhotoOnPurchaseOrderRepository {
  @Autowired
  open lateinit var database: Database

  fun findById(id: UUID): PhotoOnPurchaseOrder? {
    return database.photo_on_purchase_orders.find { it.id eq id }
  }

  fun findAll(): List<PhotoOnPurchaseOrder> {
    return database.photo_on_purchase_orders.toList()
  }
}

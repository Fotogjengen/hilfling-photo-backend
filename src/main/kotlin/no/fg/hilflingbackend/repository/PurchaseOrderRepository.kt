package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.dto.PurchaseOrderDto
import no.fg.hilflingbackend.dto.PurchaseOrderId
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.PurchaseOrder
import no.fg.hilflingbackend.model.PurchaseOrders
import no.fg.hilflingbackend.model.purchase_orders
import no.fg.hilflingbackend.value_object.Email
import no.fg.hilflingbackend.value_object.ZipCode
import org.springframework.stereotype.Repository

@Repository
open class PurchaseOrderRepository(database: Database) : BaseRepository<PurchaseOrder, PurchaseOrderDto>(table = PurchaseOrders, database = database) {
  override fun convertToClass(qrs: QueryRowSet): PurchaseOrderDto = PurchaseOrderDto(
    purchaseOrderId = PurchaseOrderId(qrs[PurchaseOrders.id]!!),
    name = qrs[PurchaseOrders.name]!!,
    email = Email(qrs[PurchaseOrders.email]!!),
    address = qrs[PurchaseOrders.address]!!,
    zipCode = ZipCode(qrs[PurchaseOrders.zipCode]!!),
    city = qrs[PurchaseOrders.city]!!,
    sendByPost = qrs[PurchaseOrders.sendByPost]!!,
    comment = qrs[PurchaseOrders.comment]!!,
    isCompleted = qrs[PurchaseOrders.isCompleted]!!,
  )

  override fun create(dto: PurchaseOrderDto): Int {
    return database.purchase_orders.add(dto.toEntity())
  }

  override fun patch(dto: PurchaseOrderDto): Int {
    return database.purchase_orders.update(dto.toEntity())
  }
}

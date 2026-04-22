package no.fg.hilflingbackend.repository

import no.fg.hilflingbackend.dto.PurchaseOrderDto
import no.fg.hilflingbackend.dto.PurchaseOrderId
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.PurchaseOrder
import no.fg.hilflingbackend.model.PurchaseOrders
import no.fg.hilflingbackend.model.purchase_orders
import no.fg.hilflingbackend.value_object.ZipCode
import org.ktorm.database.Database
import org.ktorm.dsl.QueryRowSet
import org.ktorm.entity.add
import org.ktorm.entity.update
import org.springframework.stereotype.Repository

@Repository
open class PurchaseOrderRepository(database: Database) : BaseRepository<PurchaseOrder, PurchaseOrderDto, NotImplementedError>(table = PurchaseOrders, database = database) {
  override fun convertToClass(qrs: QueryRowSet): PurchaseOrderDto = PurchaseOrderDto(
    purchaseOrderId = PurchaseOrderId(qrs[PurchaseOrders.id]!!),
    name = qrs[PurchaseOrders.name]!!,
    email = qrs[PurchaseOrders.email]!!,
    address = qrs[PurchaseOrders.address]!!,
    zipCode = ZipCode(qrs[PurchaseOrders.zipCode]!!),
    city = qrs[PurchaseOrders.city]!!,
    sendByPost = qrs[PurchaseOrders.sendByPost]!!,
    comment = qrs[PurchaseOrders.comment]!!,
    isCompleted = qrs[PurchaseOrders.isCompleted]!!
  )

  override fun create(dto: PurchaseOrderDto): Int {
    return database.purchase_orders.add(dto.toEntity())
  }

  fun patch(dto: PurchaseOrderDto): Int {
    return database.purchase_orders.update(dto.toEntity())
  }
}

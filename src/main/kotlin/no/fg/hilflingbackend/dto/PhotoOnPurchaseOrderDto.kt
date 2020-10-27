package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.PurchaseOrder
import java.util.UUID

data class PhotoOnPurchaseOrderDto(
  val photoOnPurchaseOrderId: PhotoOnPurchaseOrderId = PhotoOnPurchaseOrderId(),
  val size: String,
  val amount: Int,
  //val purchaseOrder: PurchaseOrderDto,
  //val photo: PhotoDto,
)

data class PhotoOnPurchaseOrderId(
  override val id: UUID = UUID.randomUUID()
) : UuidId {
  override fun toString(): String = id.toString()
}

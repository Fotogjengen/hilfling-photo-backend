package no.fg.hilflingbackend.dto

import jakarta.validation.constraints.Min
import java.util.UUID

data class PhotoOnPurchaseOrderDto(
  val photoOnPurchaseOrderId: PhotoOnPurchaseOrderId = PhotoOnPurchaseOrderId(),
  val size: String,
  @field:Min(value = 1, message = "Amount must be at least 1")
  val amount: Int,
)

data class PhotoOnPurchaseOrderId(
  override val id: UUID = UUID.randomUUID(),
) : UuidId {
  override fun toString(): String = id.toString()
}

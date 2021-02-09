package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.PurchaseOrder
import no.fg.hilflingbackend.value_object.Email
import no.fg.hilflingbackend.value_object.ZipCode
import java.util.UUID

data class PurchaseOrderDto(
  val purchaseOrderId: PurchaseOrderId = PurchaseOrderId(),
  val name: String,
  val email: Email,
  val address: String,
  val zipCode: ZipCode,
  val city: String,
  val sendByPost: Boolean,
  val comment: String,
  val isCompleted: Boolean
)

fun PurchaseOrderDto.toEntity(): PurchaseOrder {
  val dto = this
  return PurchaseOrder {
    id = dto.purchaseOrderId.id
    name = dto.name
    email = dto.email.value
    address = dto.address
    zipCode = dto.zipCode.value
    city = dto.city
    sendByPost = dto.sendByPost
    comment = dto.comment
    isCompleted = dto.isCompleted
  }
}

data class PurchaseOrderId(
  override val id: UUID = UUID.randomUUID()
) : UuidId {
  override fun toString(): String = id.toString()
}

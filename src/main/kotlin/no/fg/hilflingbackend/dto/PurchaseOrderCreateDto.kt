package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.value_object.Email
import no.fg.hilflingbackend.value_object.ZipCode

data class PurchaseOrderCreateDto(
  val name: String,
  val email: Email,
  val address: String,
  val zipCode: ZipCode,
  val city: String,
  val sendByPost: Boolean,
  val comment: String,
)

fun PurchaseOrderCreateDto.toFullDto() = PurchaseOrderDto(
  name = this.name,
  email = this.email,
  address = this.address,
  zipCode = this.zipCode,
  city = this.city,
  sendByPost = this.sendByPost,
  comment = this.comment,
  isCompleted = false,
)

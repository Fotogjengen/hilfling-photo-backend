package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.PurchaseOrderCreateDto
import no.fg.hilflingbackend.dto.PurchaseOrderDto
import no.fg.hilflingbackend.dto.toFullDto
import no.fg.hilflingbackend.model.PurchaseOrder
import no.fg.hilflingbackend.repository.PurchaseOrderRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/purchase_orders")
open class PurchaseOrderController(
  override val repository: PurchaseOrderRepository,
) : BaseController<PurchaseOrder, PurchaseOrderDto, NotImplementedError>(repository) {

  @PostMapping("/create")
  fun createOrder(
    @RequestBody dto: PurchaseOrderCreateDto,
  ): Int {
    return repository.create(dto.toFullDto())
  }
}

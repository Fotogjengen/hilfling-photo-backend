package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.PurchaseOrderDto
import no.fg.hilflingbackend.service.PurchaseOrderService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/purchase_orders")
class PurchaseOrderController(
  val purchaseOrderService: PurchaseOrderService,
) {
  @GetMapping("/{id}")
  fun getById(
    @PathVariable("id") id: UUID,
  ): PurchaseOrderDto = purchaseOrderService.findById(id)

  @GetMapping
  fun getAll(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
  ): Page<PurchaseOrderDto> = purchaseOrderService.findAll(page ?: 0, pageSize ?: 100)

  @PostMapping
  fun create(
    @RequestBody dto: PurchaseOrderDto,
  ): Int = purchaseOrderService.create(dto)

  @DeleteMapping("/{id}")
  fun delete(
    @PathVariable("id") id: UUID,
  ): Int = purchaseOrderService.delete(id)
}

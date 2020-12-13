package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.model.PurchaseOrder
import no.fg.hilflingbackend.repository.PurchaseOrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/purchase_orders")
class PurchaseOrderController {
  @Autowired
  lateinit var repository: PurchaseOrderRepository

  @GetMapping("/{id}")
  fun getById(@PathVariable("id") id: UUID): PurchaseOrder? {
    return repository.findById(id)
  }

  @GetMapping
  fun getAll(): List<PurchaseOrder> {
    return repository.findAll()
  }

  @PostMapping
  fun create(
    @RequestBody purchaseOrder: PurchaseOrder
  ): PurchaseOrder {
    return repository.create(purchaseOrder)
  }
}

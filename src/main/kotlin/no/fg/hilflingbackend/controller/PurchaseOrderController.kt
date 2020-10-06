package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.model.PurchaseOrder
import no.fg.hilflingbackend.repository.PurchaseOrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/purchase_orders")
class PurchaseOrderController {
    @Autowired
    lateinit var  repository: PurchaseOrderRepository

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Int) : PurchaseOrder? {
        return repository.findById(id)
    }

    @GetMapping
    fun getAll() : List<PurchaseOrder> {
        return repository.findAll()
    }

    @PostMapping
    fun create(
            @RequestBody purchaseOrder: PurchaseOrder
    ): PurchaseOrder {
        return repository.create(purchaseOrder)
    }
}
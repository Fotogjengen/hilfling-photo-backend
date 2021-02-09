package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.PurchaseOrderDto
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
open class PurchaseOrderController(override val repository: PurchaseOrderRepository) : BaseController<PurchaseOrder, PurchaseOrderDto>(repository)

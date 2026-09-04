package no.fg.hilflingbackend.service

import jakarta.persistence.EntityNotFoundException
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.PurchaseOrderDto
import no.fg.hilflingbackend.repository.PurchaseOrderRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PurchaseOrderService(
  val repository: PurchaseOrderRepository,
) {
  fun findById(id: UUID): PurchaseOrderDto = repository.findById(id) ?: throw EntityNotFoundException("PurchaseOrder $id not found")

  fun findAll(
    page: Int,
    pageSize: Int,
  ): Page<PurchaseOrderDto> = repository.findAll(page, pageSize)

  fun create(dto: PurchaseOrderDto): Int = repository.create(dto)

  fun delete(id: UUID): Int = repository.delete(id)
}

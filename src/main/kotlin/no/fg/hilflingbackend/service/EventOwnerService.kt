package no.fg.hilflingbackend.service

import jakarta.persistence.EntityNotFoundException
import no.fg.hilflingbackend.dto.EventOwnerDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.repository.EventOwnerRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class EventOwnerService(
  val repository: EventOwnerRepository,
) {
  fun findById(id: UUID): EventOwnerDto = repository.findById(id) ?: throw EntityNotFoundException("EventOwner $id not found")

  fun findAll(
    page: Int,
    pageSize: Int,
  ): Page<EventOwnerDto> = repository.findAll(page, pageSize)

  fun create(dto: EventOwnerDto): Int = repository.create(dto)

  fun delete(id: UUID): Int = repository.delete(id)
}

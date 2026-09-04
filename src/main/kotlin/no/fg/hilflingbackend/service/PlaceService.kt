package no.fg.hilflingbackend.service

import jakarta.persistence.EntityNotFoundException
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.PlaceDto
import no.fg.hilflingbackend.dto.PlacePatchRequestDto
import no.fg.hilflingbackend.repository.PlaceRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PlaceService(
  val repository: PlaceRepository,
) {
  fun findById(id: UUID): PlaceDto = repository.findById(id) ?: throw EntityNotFoundException("Place $id not found")

  fun findAll(
    page: Int,
    pageSize: Int,
  ): Page<PlaceDto> = repository.findAll(page, pageSize)

  fun create(dto: PlaceDto): Int = repository.create(dto)

  fun delete(id: UUID): Int = repository.delete(id)

  fun patch(dto: PlacePatchRequestDto): PlaceDto = repository.patch(dto)
}

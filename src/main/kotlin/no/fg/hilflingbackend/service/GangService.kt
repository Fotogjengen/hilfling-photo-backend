package no.fg.hilflingbackend.service

import jakarta.persistence.EntityNotFoundException
import no.fg.hilflingbackend.dto.GangDto
import no.fg.hilflingbackend.dto.GangPatchRequestDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.repository.GangRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GangService(
  val repository: GangRepository,
) {
  fun findById(id: UUID): GangDto = repository.findById(id) ?: throw EntityNotFoundException("Gang $id not found")

  fun findAll(
    page: Int,
    pageSize: Int,
  ): Page<GangDto> = repository.findAll(page, pageSize)

  fun create(dto: GangDto): Int = repository.create(dto)

  fun delete(id: UUID): Int = repository.delete(id)

  fun patch(dto: GangPatchRequestDto): GangDto = repository.patch(dto)
}

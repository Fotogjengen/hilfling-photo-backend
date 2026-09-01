package no.fg.hilflingbackend.service

import jakarta.persistence.EntityNotFoundException
import no.fg.hilflingbackend.dto.AlbumDto
import no.fg.hilflingbackend.dto.AlbumPatchRequestDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.repository.AlbumRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AlbumService(
  val repository: AlbumRepository,
) {
  fun findById(id: UUID): AlbumDto = repository.findById(id) ?: throw EntityNotFoundException("Album $id not found")

  fun findAll(
    page: Int,
    pageSize: Int,
  ): Page<AlbumDto> = repository.findAll(page, pageSize)

  fun create(dto: AlbumDto): Int = repository.create(dto)

  fun delete(id: UUID): Int = repository.delete(id)

  fun patch(dto: AlbumPatchRequestDto): AlbumDto = repository.patch(dto)

  fun findAllAnalog(
    page: Int,
    pageSize: Int,
  ): Page<AlbumDto> = repository.findAllAnalog(page, pageSize)

  fun findAllDigital(
    page: Int,
    pageSize: Int,
  ): Page<AlbumDto> = repository.findAllDigital(page, pageSize)
}

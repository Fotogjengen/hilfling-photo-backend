package no.fg.hilflingbackend.service

import jakarta.persistence.EntityNotFoundException
import no.fg.hilflingbackend.dto.ExternalUserDto
import no.fg.hilflingbackend.dto.ExternalUserPatchRequestDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.repository.ExternalUserRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ExternalUserService(
  val repository: ExternalUserRepository,
) {
  private fun ExternalUserDto.stripPassword(): ExternalUserDto = copy(password = null)

  fun findById(id: UUID): ExternalUserDto =
    (repository.findById(id) ?: throw EntityNotFoundException("ExternalUser $id not found")).stripPassword()

  fun findAll(
    page: Int,
    pageSize: Int,
  ): Page<ExternalUserDto> = repository.findAll(page, pageSize).let { page ->
    page.copy(currentList = page.currentList.map { it.stripPassword() })
  }

  fun create(dto: ExternalUserDto): Int = repository.create(dto)

  fun delete(id: UUID): Int = repository.delete(id)

  fun patch(dto: ExternalUserPatchRequestDto): ExternalUserDto = repository.patch(dto).stripPassword()
}

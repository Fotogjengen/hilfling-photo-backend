package no.fg.hilflingbackend.service

import jakarta.persistence.EntityNotFoundException
import no.fg.hilflingbackend.dto.ExternalUserDto
import no.fg.hilflingbackend.dto.ExternalUserPatchRequestDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.repository.ExternalUserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class ExternalUserService(
  val repository: ExternalUserRepository,
) {
  private fun ExternalUserDto.stripPassword(): ExternalUserDto = copy(password = null)

  private fun ensureUsernameAvailable(
    username: String,
    excludeId: UUID? = null,
  ) {
    if (repository.isUsernameTaken(username, excludeId)) {
      throw ResponseStatusException(HttpStatus.CONFLICT, "Username $username is already taken")
    }
  }

  fun findById(id: UUID): ExternalUserDto = (repository.findById(id) ?: throw EntityNotFoundException("ExternalUser $id not found")).stripPassword()

  fun findAll(
    page: Int,
    pageSize: Int,
  ): Page<ExternalUserDto> =
    repository.findAll(page, pageSize).let { page ->
      page.copy(currentList = page.currentList.map { it.stripPassword() })
    }

  fun create(dto: ExternalUserDto): Int {
    require(!dto.password.isNullOrBlank()) { "Password is required" }
    ensureUsernameAvailable(dto.username)
    return repository.create(dto)
  }

  fun delete(id: UUID): Int = repository.delete(id)

  fun patch(dto: ExternalUserPatchRequestDto): ExternalUserDto {
    dto.username?.let { ensureUsernameAvailable(it, dto.externalUserId.id) }
    return repository.patch(dto).stripPassword()
  }

  fun resetPassword(
    id: UUID,
    password: String,
  ) {
    require(password.isNotBlank()) { "Password cannot be blank" }
    if (repository.updatePassword(id, password) == 0) {
      throw EntityNotFoundException("ExternalUser $id not found")
    }
  }
}

package no.fg.hilflingbackend.service

import jakarta.persistence.EntityNotFoundException
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.PositionDto
import no.fg.hilflingbackend.dto.PositionId
import no.fg.hilflingbackend.dto.PositionPatchRequestDto
import no.fg.hilflingbackend.repository.PositionRepository
import no.fg.hilflingbackend.valueobject.Permission
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PositionService(
  val repository: PositionRepository,
) {
  fun findById(id: UUID): PositionDto = repository.findById(id) ?: throw EntityNotFoundException("Position $id not found")

  fun findAll(
    page: Int,
    pageSize: Int,
  ): Page<PositionDto> = repository.findAll(page, pageSize)

  fun create(dto: PositionDto): Int = repository.create(dto)

  fun delete(id: UUID): Int = repository.delete(id)

  fun patch(dto: PositionPatchRequestDto): PositionDto = repository.patch(dto)

  fun addPermissionToPosition(
    positionId: PositionId,
    permission: Permission,
  ): Int =
    repository.addPermissionToPosition(
      positionId,
      permission,
    )

  fun removePermissionFromPosition(
    positionId: PositionId,
    permission: Permission,
  ): Int =
    repository.removePermissionFromPosition(
      positionId,
      permission,
    )

  fun findPermissionsByPositionId(positionId: PositionId): List<Permission> = repository.findPermissionsByPositionId(positionId)
}

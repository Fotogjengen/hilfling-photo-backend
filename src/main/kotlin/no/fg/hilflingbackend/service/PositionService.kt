package no.fg.hilflingbackend.service

import no.fg.hilflingbackend.repository.PositionRepository
import no.fg.hilflingbackend.dto.PositionId
import org.springframework.stereotype.Service
import no.fg.hilflingbackend.valueobject.Permission

@Service
class PositionService(
  val repository: PositionRepository,
) {
  fun addPermissionToPosition(positionId: PositionId, permission: Permission): Int {
    return repository.addPermissionToPosition(
      positionId,
      permission,
    )
  }

  fun removePermissionFromPosition(positionId: PositionId, permission: Permission): Int {
    return repository.removePermissionFromPosition(
      positionId,
      permission,
    )
  }

  fun findPermissionsByPositionId(positionId: PositionId): List<Permission> {
    return repository.findPermissionsByPositionId(positionId)
  }
}

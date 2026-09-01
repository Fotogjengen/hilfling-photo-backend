package no.fg.hilflingbackend.service

import no.fg.hilflingbackend.dto.JwtTokenPayload
import no.fg.hilflingbackend.repository.PhotoGangBangerRepository
import no.fg.hilflingbackend.valueobject.Permission
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import org.springframework.stereotype.Service

@Service
class AuthService(
  val jwtService: JwtService,
  val photoGangBangerRepository: PhotoGangBangerRepository,
  val positionService: PositionService,
) {
  fun login(username: String): String {
    val fgUser = photoGangBangerRepository.findByUsername(username)

    if (fgUser != null) {
      val activePosition = fgUser.positions.firstOrNull { it.isActive }
      val permissions: List<Permission> =
        activePosition?.let { positionService.findPermissionsByPositionId(it.positionId) } ?: emptyList()
      return jwtService.generateToken(
        JwtTokenPayload(
          username = username,
          positionId = activePosition?.positionId?.toString(),
          securityLevel = SecurityLevelType.FG,
          permissions = permissions,
        ),
      )
    }

    return jwtService.generateToken(
      JwtTokenPayload(
        username = username,
        positionId = null,
        securityLevel = SecurityLevelType.HUSFOLK,
      ),
    )
  }
}

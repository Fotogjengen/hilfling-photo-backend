package no.fg.hilflingbackend.service

import no.fg.hilflingbackend.dto.JwtTokenPayload
import no.fg.hilflingbackend.repository.PhotoGangBangerRepository
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import org.springframework.stereotype.Service

@Service
class AuthService(
  val jwtService: JwtService,
  val photoGangBangerRepository: PhotoGangBangerRepository,
) {
  fun login(username: String): String {
    val fgUser = photoGangBangerRepository.findByUsername(username)

    if (fgUser != null) {
      return jwtService.generateToken(
        JwtTokenPayload(
          username = username,
          positionId =
            fgUser.positions
              .firstOrNull { it.isActive }
              ?.positionId
              ?.toString(),
          securityLevel = SecurityLevelType.FG,
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

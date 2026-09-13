package no.fg.hilflingbackend.service

import no.fg.hilflingbackend.dto.JwtTokenPayload
import no.fg.hilflingbackend.repository.ExternalUserRepository
import no.fg.hilflingbackend.repository.PhotoGangBangerRepository
import no.fg.hilflingbackend.valueobject.Permission
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
  val jwtService: JwtService,
  val photoGangBangerRepository: PhotoGangBangerRepository,
  val positionService: PositionService,
  val externalUserRepository: ExternalUserRepository,
  val passwordEncoder: PasswordEncoder,
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

  fun loginExternalUser(
    username: String,
    password: String,
  ): String {
    val externalUser =
      externalUserRepository.findByUsername(username)
        ?: throw IllegalArgumentException("Invalid username or password")

    if (!externalUser.isActive) {
      throw IllegalArgumentException("Invalid username or password")
    }

    val storedPassword = externalUser.password
    if (storedPassword == null || !passwordEncoder.matches(password, storedPassword)) {
      throw IllegalArgumentException("Invalid username or password")
    }

    val securityLevel = externalUser.securityLevel?.securityLevelType ?: SecurityLevelType.ALLE

    return jwtService.generateToken(
      JwtTokenPayload(
        username = username,
        positionId = null,
        securityLevel = securityLevel,
        isExternalUser = true,
      ),
    )
  }
}

package no.fg.hilflingbackend.service

import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.repository.GangRepository
import no.fg.hilflingbackend.repository.PhotoGangBangerRepository
import no.fg.hilflingbackend.repository.PositionRepository
import no.fg.hilflingbackend.repository.SamfundetUserRepository
import no.fg.hilflingbackend.value_object.SecurityLevelType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.stereotype.Service

/** Service class for handlig auth-releated oprations such as login and permission checks */
@Service
class AuthService(
        val jwtService: JwtService,
        val gangRepository: GangRepository,
        val positionRepository: PositionRepository,
        val photoGangBangerRepository: PhotoGangBangerRepository,
        val authenticationManager: AuthenticationManager,
        val samfundetUserRepository: SamfundetUserRepository,
) {
    /** Handles user login and generates a JWT token */
    fun login(username: String): String? {
        val samfunderUser = samfundetUserRepository.findByUsername(username) ?: return null

        val fgUser =
                photoGangBangerRepository.findBySamfundetUserId(samfunderUser.samfundetUserId.id)
        val positionId = fgUser?.position?.positionId?.id ?: return null
        val position = positionRepository.findById(positionId)?.toEntity() ?: return null

        // TODO: dette må ryddes opp i.
        // Hva er samfundetUser en gang. Vi trenger jo ikke det siden alle login requests egentlig
        // er pre-autentisert som en samfunder bruker.
        // Jeg har dette foreløpig, men før lansering må vi fikse opp i den del her hehe
        val securityLevel = samfunderUser.securityLevel.securityLevelType ?: SecurityLevelType.ALLE

        return jwtService.generateToken(username, position, securityLevel)
    }
}

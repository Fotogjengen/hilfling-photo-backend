package no.fg.hilflingbackend.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import no.fg.hilflingbackend.dto.JwtTokenPayload
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.Date
import java.util.function.Function
import javax.crypto.SecretKey

@Service
class JwtService(
  @Value("\${jwt.secret}") jwtSecretB64: String,
) {
  private val secretKey: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretB64))

  fun generateToken(payload: JwtTokenPayload): String {
    val claims =
      mapOf(
        "username" to payload.username,
        "positionId" to payload.positionId,
        "securityLevel" to payload.securityLevel,
      )

    return Jwts
      .builder()
      .claims()
      .add(claims)
      .subject(payload.username)
      .issuedAt(Date(System.currentTimeMillis()))
      .expiration(Date(System.currentTimeMillis() + 1000 * 5 * 60 * 60))
      // TODO: this should be coordinated with ITK, so that our token expires at the
      // same time as theirs
      .and()
      .signWith(secretKey)
      .compact()
  }

  fun extractPayload(token: String): JwtTokenPayload {
    val claims = extractAllClaims(token)
    return JwtTokenPayload(
      username = claims.subject,
      positionId = claims.get("positionId", String::class.java),
      securityLevel = SecurityLevelType.valueOf(claims.get("securityLevel", String::class.java)),
    )
  }

  fun allowedSecurityLevels(bearerHeader: String?): List<String> {
    if (bearerHeader == null) return listOf("ALLE")
    val token = bearerHeader.removePrefix("Bearer ").trim()
    return try {
      when (extractPayload(token).securityLevel) {
        SecurityLevelType.FG -> listOf("FG", "HUSFOLK", "ALLE")
        SecurityLevelType.HUSFOLK -> listOf("HUSFOLK", "ALLE")
        else -> listOf("ALLE")
      }
    } catch (e: Exception) {
      listOf("ALLE")
    }
  }

  fun isTokenValid(token: String): Boolean =
    try {
      !isTokenExpired(token)
    } catch (e: Exception) {
      false
    }

  fun validateToken(
    token: String,
    userDetails: UserDetails,
  ): Boolean {
    val payload = extractPayload(token)
    return payload.username == userDetails.username && !isTokenExpired(token)
  }

  private fun extractAllClaims(token: String): Claims =
    Jwts
      .parser()
      .verifyWith(secretKey)
      .build()
      .parseSignedClaims(token)
      .payload

  private fun <T> extractClaim(
    token: String,
    claimResolver: Function<Claims, T>,
  ): T = claimResolver.apply(extractAllClaims(token))

  private fun isTokenExpired(token: String): Boolean = extractExpiration(token).before(Date())

  private fun extractExpiration(token: String): Date = extractClaim(token, Claims::getExpiration)
}

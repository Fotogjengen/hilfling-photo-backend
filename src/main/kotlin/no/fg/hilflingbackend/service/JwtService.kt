package no.fg.hilflingbackend.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import no.fg.hilflingbackend.dto.JwtTokenPayload
import no.fg.hilflingbackend.valueobject.Permission
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.math.BigInteger
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.PrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import java.util.Date
import java.util.function.Function

@Service
class JwtService(
  @Value("\${jwt.private-key}") jwtPrivateKeyPem: String,
  @Value("\${jwt.public-key}") jwtPublicKeyPem: String,
) {
  private val privateKey: PrivateKey = parsePrivateKey(jwtPrivateKeyPem)
  private val publicKey: RSAPublicKey = parsePublicKey(jwtPublicKeyPem)

  // stable key id derived from the public key
  private val keyId: String = thumbprint(publicKey)

  fun generateToken(payload: JwtTokenPayload): String {
    val claims =
      mapOf(
        "username" to payload.username,
        "positionId" to payload.positionId,
        "securityLevel" to payload.securityLevel,
        "permissions" to payload.permissions.map { it.name },
      )

    return Jwts
      .builder()
      .header()
      .keyId(keyId)
      .and()
      .claims()
      .add(claims)
      .subject(payload.username)
      .issuedAt(Date(System.currentTimeMillis()))
      .expiration(Date(System.currentTimeMillis() + 1000 * 5 * 60 * 60))
      // TODO: this should be coordinated with ITK, so that our token expires at the
      // same time as theirs
      .and()
      .signWith(privateKey, Jwts.SIG.RS256)
      .compact()
  }

  /**
   * returns the JWKS document containing our public key, so that verifiers can validate our tokens
   */
  fun jwks(): Map<String, Any> =
    mapOf(
      "keys" to
        listOf(
          mapOf(
            "kty" to "RSA",
            "use" to "sig",
            "alg" to "RS256",
            "kid" to keyId,
            "n" to base64Url(toUnsignedBytes(publicKey.modulus)),
            "e" to base64Url(toUnsignedBytes(publicKey.publicExponent)),
          ),
        ),
    )

  fun extractSecurityLevel(token: String?): SecurityLevelType {
    if (token == null) return SecurityLevelType.ALLE
    return try {
      extractPayload(token).securityLevel
    } catch (e: Exception) {
      SecurityLevelType.ALLE
    }
  }

  fun extractPayload(token: String): JwtTokenPayload {
    val claims = extractAllClaims(token)
    return JwtTokenPayload(
      username = claims.subject,
      positionId = claims.get("positionId", String::class.java),
      securityLevel = SecurityLevelType.valueOf(claims.get("securityLevel", String::class.java)),
      permissions = claims.get("permissions", List::class.java)
        ?.mapNotNull { runCatching { Permission.valueOf(it.toString()) }.getOrNull() }
        ?: emptyList(),
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
      .verifyWith(publicKey)
      .build()
      .parseSignedClaims(token)
      .payload

  private fun <T> extractClaim(
    token: String,
    claimResolver: Function<Claims, T>,
  ): T = claimResolver.apply(extractAllClaims(token))

  private fun isTokenExpired(token: String): Boolean = extractExpiration(token).before(Date())

  private fun extractExpiration(token: String): Date = extractClaim(token, Claims::getExpiration)

  companion object {
    private fun decodePem(pem: String): ByteArray {
      val body =
        pem
          .replace("-----BEGIN PRIVATE KEY-----", "")
          .replace("-----END PRIVATE KEY-----", "")
          .replace("-----BEGIN PUBLIC KEY-----", "")
          .replace("-----END PUBLIC KEY-----", "")
          .replace(Regex("\\s"), "")
      return Base64.getDecoder().decode(body)
    }

    private fun parsePrivateKey(pem: String): PrivateKey =
      KeyFactory
        .getInstance("RSA")
        .generatePrivate(PKCS8EncodedKeySpec(decodePem(pem)))

    private fun parsePublicKey(pem: String): RSAPublicKey =
      KeyFactory
        .getInstance("RSA")
        .generatePublic(X509EncodedKeySpec(decodePem(pem))) as RSAPublicKey

    private fun base64Url(bytes: ByteArray): String = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)

    // BigInteger.toByteArray() may prepend a sign byte; JWK expects the
    // minimal unsigned big-endian representation.
    private fun toUnsignedBytes(value: BigInteger): ByteArray {
      val bytes = value.toByteArray()
      return if (bytes.size > 1 && bytes[0].toInt() == 0) bytes.copyOfRange(1, bytes.size) else bytes
    }

    private fun thumbprint(key: RSAPublicKey): String {
      val digest = MessageDigest.getInstance("SHA-256").digest(key.encoded)
      return base64Url(digest)
    }
  }
}

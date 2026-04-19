package no.fg.hilflingbackend.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import java.util.Base64
import java.util.Date
import java.util.function.Function
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import no.fg.hilflingbackend.model.Position
import no.fg.hilflingbackend.value_object.SecurityLevelType
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

/**
 * Service class for handling JWT (JSON Web Token) operations. This class provides methods to
 * generate, validate, and extract information from JWTs.
 */
@Service
class JwtService {

    private var secretKey: String = ""

    /** Constructor that initializes the secret key for signing JWTs. */
    init {
        val keyGen = KeyGenerator.getInstance("HmacSHA256")
        val sk: SecretKey = keyGen.generateKey()
        secretKey = Base64.getEncoder().encodeToString(sk.encoded)
    }

    /**
     * Generates a JWT token with the specified username, positionId, email and securityLevel
     *
     * @param username the username to include in the token
     * @param positionId the position id to determine internal access
     * @param email the email of the user
     * @param securityLevel the security level of the user
     * @return the generated JWT token as a string
     */
    fun generateToken(
            username: String,
            position: Position?,
            securityLevel: SecurityLevelType,
    ): String {
        val claims =
                mapOf(
                        "username" to username,
                        "position" to position,
                        "securityLevel" to securityLevel
                )

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(Date(System.currentTimeMillis()))
                .expiration(Date(System.currentTimeMillis() + 1000 * 5 * 60 * 60))
                // TODO: this should be coordinated with ITK, so that our token expires at the
                // same time as theirs
                .and()
                .signWith(getKey())
                .compact()
    }

    private fun getKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token the JWT token to extract the username from
     * @return the username extracted from the token
     */
    fun extractUserName(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }

    /**
     * Extracts the position ID from the given JWT token
     *
     * @param token the JWT token to extract the user ID from
     * @return the position ID extracted from the token
     */
    fun extractPositionId(token: String): String {
        return extractClaim(token) { claims -> claims.get("positionId", String::class.java) }
    }

    /**
     * Extracts the security level of the JWT token
     *
     * @param token the JWT token to extract the isAdmin claim from
     * @return the securityLevel value extracted from the token
     */
    fun extractSecurityLevel(token: String): String {
        return extractClaim(token) { claims -> claims.get("securityLevel", String::class.java) }
    }

    private fun <T> extractClaim(token: String, claimResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimResolver.apply(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).payload
    }

    /**
     * Validates the given JWT token against the provided user details.
     *
     * @param token the JWT token to validate
     * @param userDetails the user details to validate against
     * @return true if the token is valid, false otherwise
     */
    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val userName = extractUserName(token)
        return userName == userDetails.username && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }
}

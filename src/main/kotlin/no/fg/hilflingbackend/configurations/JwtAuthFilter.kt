package no.fg.hilflingbackend.configurations

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import no.fg.hilflingbackend.service.JwtService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * JWT Filter that intercepts HTTP requests to extract and validate JWT tokens from the
 * Authorization header. If the token is valid, it sets the authenticated user in the Spring
 * SecurityContext.
 */
@Component
class JwtAuthFilter : OncePerRequestFilter() {
  @Autowired private lateinit var jwtService: JwtService

  private val log = LoggerFactory.getLogger(JwtAuthFilter::class.java)

  override fun doFilterInternal(
    request: HttpServletRequest,
    response: HttpServletResponse,
    filterChain: FilterChain,
  ) {
    val token = request.getHeader("X-hilfling-token")
    if (token == null) {
      filterChain.doFilter(request, response)
      return
    }

    try {
      val payload = jwtService.extractPayload(token)
      if (SecurityContextHolder.getContext().authentication == null &&
        jwtService.isTokenValid(token)
      ) {
        val authorities = listOf(SimpleGrantedAuthority("ROLE_${payload.securityLevel.type}"))
        val authToken = UsernamePasswordAuthenticationToken(payload.username, null, authorities)
        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authToken
      }
    } catch (e: ExpiredJwtException) {
      log.warn("JWT token expired: {}", e.message)
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired")
      return
    } catch (e: SignatureException) {
      log.warn("JWT signature invalid: {}", e.message)
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token signature")
      return
    } catch (e: MalformedJwtException) {
      log.warn("JWT token malformed: {}", e.message)
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Malformed token")
      return
    } catch (e: UnsupportedJwtException) {
      log.warn("JWT token unsupported: {}", e.message)
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unsupported token")
      return
    }

    filterChain.doFilter(request, response)
  }
}

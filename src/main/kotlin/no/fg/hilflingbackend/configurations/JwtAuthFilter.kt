package no.fg.hilflingbackend.configurations

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import no.fg.hilflingbackend.service.JwtService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
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
    @Autowired private lateinit var userDetailsService: UserDetailsService

    override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            filterChain: FilterChain
    ) {

        print(request)

        val authHeader = request.getHeader("X-hilfling-token")
        if (authHeader == null) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader.substring(7)
        val username = jwtService.extractUserName(token)

        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(username)
            if (jwtService.validateToken(token, userDetails)) {
                val authToken =
                        UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.authorities
                        )
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
            }
        }

        filterChain.doFilter(request, response)
    }
}

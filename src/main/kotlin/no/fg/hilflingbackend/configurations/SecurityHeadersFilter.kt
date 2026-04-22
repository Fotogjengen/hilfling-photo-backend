package no.fg.hilflingbackend.configurations

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(1)
class SecurityHeadersFilter : Filter {
  override fun doFilter(
    request: ServletRequest,
    response: ServletResponse,
    chain: FilterChain,
  ) {
    val httpResponse = response as HttpServletResponse
    httpResponse.setHeader("X-Content-Type-Options", "nosniff")
    httpResponse.setHeader("X-Frame-Options", "DENY")
    httpResponse.setHeader("X-XSS-Protection", "0")
    httpResponse.setHeader("Content-Security-Policy", "default-src 'self'")
    httpResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains")
    chain.doFilter(request, response)
  }
}

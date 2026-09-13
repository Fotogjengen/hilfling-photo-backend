package no.fg.hilflingbackend.configurations

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

/**
 * Interceptor for handling security levels with the @RequireSecurityLevel annotation.
 * Grants access when the user's security level is at least as privileged as the
 * required one.
 */
@Component
class SecurityLevelInterceptor : HandlerInterceptor {
  override fun preHandle(
    request: HttpServletRequest,
    response: HttpServletResponse,
    handler: Any,
  ): Boolean {
    if (handler !is HandlerMethod) return true
    val required = handler.getMethodAnnotation(RequireSecurityLevel::class.java) ?: return true

    val auth = SecurityContextHolder.getContext().authentication
    if (auth == null) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthenticated")
      return false
    }

    val held = auth.authorities.map { it.authority }.toSet()
    if ("ROLE_EXTERNAL_USER" in held && !required.allowExternalUsers) {
      response.sendError(HttpServletResponse.SC_FORBIDDEN, "External users are not allowed")
      return false
    }
    val userLevel =
      SecurityLevelType.entries.firstOrNull { "ROLE_${it.type}" in held }
    if (userLevel == null || userLevel.ordinal > required.value.ordinal) {
      response.sendError(HttpServletResponse.SC_FORBIDDEN, "Insufficient security level")
      return false
    }
    return true
  }
}

package no.fg.hilflingbackend.configurations

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

/**
 * Interceptor for handling permissions with the @RequirePermission annotation
 */
@Component
class PermissionInterceptor : HandlerInterceptor {
  override fun preHandle(
    request: HttpServletRequest,
    response: HttpServletResponse,
    handler: Any,
  ): Boolean {
    if (handler !is HandlerMethod) return true
    val required = handler.getMethodAnnotation(RequirePermission::class.java) ?: return true

    val auth = SecurityContextHolder.getContext().authentication
    if (auth == null) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthenticated")
      return false
    }

    val held = auth.authorities.map { it.authority }.toSet()
    val hasAny = required.value.any { it.name in held }
    if (!hasAny) {
      response.sendError(HttpServletResponse.SC_FORBIDDEN, "Missing required permission")
      return false
    }
    return true
  }
}

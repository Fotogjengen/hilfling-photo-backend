package no.fg.hilflingbackend.configurations

import jakarta.servlet.http.HttpServletRequest

/**
 * The auth token cookie name
 */
const val TOKEN_COOKIE = "fgToken"

/**
 * Extracts the JWT from the request. fgToken if it exists, otherwise X-hilfling-token header
 */
fun HttpServletRequest.hilflingToken(): String? {
  val fromCookie = cookies?.firstOrNull { it.name == TOKEN_COOKIE }?.value
  return fromCookie ?: getHeader("X-hilfling-token")
}

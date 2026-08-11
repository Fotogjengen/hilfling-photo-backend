package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.service.JwtService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Publishes our JWT signing public key as a JWKS document so verifiers can validate our tokens
 */
@RestController
class JwksController(
  private val jwtService: JwtService,
) {
  @GetMapping("/.well-known/jwks.json")
  fun jwks(): Map<String, Any> = jwtService.jwks()
}

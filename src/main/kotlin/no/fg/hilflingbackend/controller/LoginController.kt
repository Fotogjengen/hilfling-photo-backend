package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class LoginController(
  private val authService: AuthService,
) {
  data class LoginResponse(
    val token: String,
  )

  data class ExternalLoginRequest(
    val username: String,
    val password: String,
  )

  // login from the ITK proxy
  // IMPORTANT: this endpoint should be protected to never ever be called from outside the ITK proxy
  @PostMapping("/login")
  fun login(
    @RequestHeader("X-Samfundet-Remote-User") username: String,
  ): ResponseEntity<LoginResponse> {
    val token = authService.login(username)
    return ResponseEntity.ok(LoginResponse(token))
  }

  @PostMapping("/external-login")
  fun externalLogin(
    @RequestBody request: ExternalLoginRequest,
  ): ResponseEntity<LoginResponse> {
    val token = authService.loginExternalUser(request.username, request.password)
    return ResponseEntity.ok(LoginResponse(token))
  }
}

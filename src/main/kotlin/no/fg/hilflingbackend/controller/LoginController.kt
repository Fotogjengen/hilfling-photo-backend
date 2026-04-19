package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class LoginController(
        private val authService: AuthService,
) {

    data class LoginRequest(
            val username: String,
    )

    data class LoginResponse(
            val token: String,
    )

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val token = authService.login(request.username)
        return if (token != null) {
            ResponseEntity.ok(LoginResponse(token))
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }
}

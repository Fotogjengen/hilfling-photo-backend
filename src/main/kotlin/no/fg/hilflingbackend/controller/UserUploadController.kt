package no.fg.hilflingbackend.controller

import jakarta.servlet.http.HttpServletRequest
import no.fg.hilflingbackend.configurations.RequireSecurityLevel
import no.fg.hilflingbackend.configurations.hilflingToken
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.UserUploadDto
import no.fg.hilflingbackend.dto.UserUploadRequestDto
import no.fg.hilflingbackend.service.JwtService
import no.fg.hilflingbackend.service.UserUploadService
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/user-uploads")
class UserUploadController(
  val userUploadService: UserUploadService,
  val jwtService: JwtService,
) {
  @PostMapping
  @RequireSecurityLevel(SecurityLevelType.FG)
  fun register(
    @RequestBody dto: UserUploadRequestDto,
    request: HttpServletRequest,
  ): UserUploadDto {
    val username = jwtService.extractPayload(request.hilflingToken()!!).username
    return userUploadService.register(dto, username)
  }

  @GetMapping("/{id}")
  fun getById(
    @PathVariable id: UUID,
    request: HttpServletRequest,
  ): UserUploadDto {
    val securityLevel = jwtService.extractSecurityLevel(request.hilflingToken())
    return userUploadService.findById(id, securityLevel)
  }

  @GetMapping
  @RequireSecurityLevel(SecurityLevelType.FG)
  fun getMyUploads(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
    request: HttpServletRequest,
  ): Page<UserUploadDto> {
    val payload = jwtService.extractPayload(request.hilflingToken()!!)
    return userUploadService.findAll(page ?: 0, pageSize ?: 10, payload.username, payload.permissions)
  }

  @DeleteMapping("/{id}")
  @RequireSecurityLevel(SecurityLevelType.FG)
  fun delete(
    @PathVariable id: UUID,
    request: HttpServletRequest,
  ): UserUploadDto {
    val payload = jwtService.extractPayload(request.hilflingToken()!!)
    return userUploadService.delete(id, payload.username, payload.permissions)
  }
}

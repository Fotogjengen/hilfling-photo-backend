package no.fg.hilflingbackend.controller

import jakarta.servlet.http.HttpServletRequest
import no.fg.hilflingbackend.dto.MotiveCreateRequestDto
import no.fg.hilflingbackend.dto.MotiveDefaultsDto
import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.dto.MotivePatchRequestDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.service.JwtService
import no.fg.hilflingbackend.service.MotiveService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/motives")
class MotiveController(
  val motiveService: MotiveService,
  val jwtService: JwtService,
) {
  @GetMapping("/{id}")
  fun getById(
    @PathVariable("id") id: UUID,
    request: HttpServletRequest,
  ): MotiveDto {
    val securityLevel = jwtService.extractSecurityLevel(request.getHeader("X-hilfling-token"))
    return motiveService.findById(id, securityLevel)
  }

  @GetMapping
  fun getAll(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
    request: HttpServletRequest,
  ): Page<MotiveDto> {
    val securityLevel = jwtService.extractSecurityLevel(request.getHeader("X-hilfling-token"))
    return motiveService.findAll(page ?: 0, pageSize ?: 10, securityLevel)
  }

  @GetMapping("/search/{searchTerm}")
  fun search(
    @PathVariable("searchTerm") searchTerm: String,
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
    request: HttpServletRequest,
  ): Page<MotiveDto> {
    val securityLevel = jwtService.extractSecurityLevel(request.getHeader("X-hilfling-token"))
    return motiveService.search(searchTerm, page ?: 0, pageSize ?: 10, securityLevel)
  }

  @PostMapping
  fun create(
    @RequestBody dto: MotiveCreateRequestDto,
    request: HttpServletRequest,
  ): MotiveDto {
    val securityLevel = jwtService.extractSecurityLevel(request.getHeader("X-hilfling-token"))
    return motiveService.create(dto, securityLevel)
  }

  @PatchMapping
  fun patch(
    @RequestBody dto: MotivePatchRequestDto,
    request: HttpServletRequest,
  ): MotiveDto {
    val securityLevel = jwtService.extractSecurityLevel(request.getHeader("X-hilfling-token"))
    return motiveService.patch(dto, securityLevel)
  }

  @DeleteMapping("/{id}")
  fun delete(
    @PathVariable("id") id: UUID,
    request: HttpServletRequest,
  ): Int {
    val securityLevel = jwtService.extractSecurityLevel(request.getHeader("X-hilfling-token"))
    return motiveService.deleteMotive(id, securityLevel)
  }

  @GetMapping("/defaults")
  fun getDefaults(): MotiveDefaultsDto = motiveService.getMotiveDefaults()
}

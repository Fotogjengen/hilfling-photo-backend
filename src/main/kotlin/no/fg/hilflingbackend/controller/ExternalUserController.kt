package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.configurations.RequirePermission
import no.fg.hilflingbackend.dto.ExternalUserDto
import no.fg.hilflingbackend.dto.ExternalUserPatchRequestDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.service.ExternalUserService
import no.fg.hilflingbackend.valueobject.Permission
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
@RequestMapping("/external-users")
class ExternalUserController(
  val externalUserService: ExternalUserService,
) {
  @GetMapping("/{id}")
  fun getById(
    @PathVariable("id") id: UUID,
  ): ExternalUserDto = externalUserService.findById(id)

  @GetMapping
  fun getAll(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
  ): Page<ExternalUserDto> = externalUserService.findAll(page ?: 0, pageSize ?: 100)

  @PostMapping
  @RequirePermission(Permission.USER_MANAGE)
  fun create(
    @RequestBody dto: ExternalUserDto,
  ): Int = externalUserService.create(dto)

  @DeleteMapping("/{id}")
  @RequirePermission(Permission.USER_MANAGE)
  fun delete(
    @PathVariable("id") id: UUID,
  ): Int = externalUserService.delete(id)

  @PatchMapping
  @RequirePermission(Permission.USER_MANAGE)
  fun patch(
    @RequestBody dto: ExternalUserPatchRequestDto,
  ): ExternalUserDto = externalUserService.patch(dto)
}

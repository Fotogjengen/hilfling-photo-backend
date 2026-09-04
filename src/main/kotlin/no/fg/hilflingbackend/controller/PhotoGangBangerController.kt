package no.fg.hilflingbackend.controller

import hilfling.backend.hilfling.exceptions.RestExceptionHandler
import no.fg.hilflingbackend.configurations.RequirePermission
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.PhotoGangBangerDto
import no.fg.hilflingbackend.dto.PhotoGangBangerPatchRequestDto
import no.fg.hilflingbackend.dto.PhotoGangBangerPositionPatchRequestDto
import no.fg.hilflingbackend.repository.PhotoGangBangerRepository
import no.fg.hilflingbackend.utils.ResponseCreated
import no.fg.hilflingbackend.valueobject.Permission
import org.springframework.http.ResponseEntity
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
@RequestMapping("/photo_gang_bangers")
class PhotoGangBangerController(
  val repository: PhotoGangBangerRepository,
) : RestExceptionHandler() {
  @GetMapping("/{id}")
  fun getById(
    @PathVariable("id") id: UUID,
  ): PhotoGangBangerDto? = repository.findById(id)

  @GetMapping
  fun getAll(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
  ): Page<PhotoGangBangerDto> = repository.findAll(page = 0, pageSize = 100)

  @GetMapping("/actives")
  fun getActives(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
  ): Page<PhotoGangBangerDto> = repository.findAllActives(page = 0, pageSize = 100)

  @GetMapping("/active_pangs")
  fun getActivePangs(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
  ): Page<PhotoGangBangerDto> = repository.findAllActivePangs(page = 0, pageSize = 100)

  @GetMapping("/inactive_pangs")
  fun getInActivePangs(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
  ): Page<PhotoGangBangerDto> = repository.findAllInactivePangs(page = 0, pageSize = 100)

  @PostMapping
  @RequirePermission(Permission.USER_MANAGE)
  fun create(
    @RequestBody dto: PhotoGangBangerDto,
  ): ResponseEntity<Int> {
    val created = repository.create(dto)
    return ResponseCreated(created)
  }

  @PatchMapping()
  @RequirePermission(Permission.USER_MANAGE)
  fun patch(
    @RequestBody dto: PhotoGangBangerPatchRequestDto,
  ): PhotoGangBangerDto? = repository.patch(dto)

  @PatchMapping("/positions")
  @RequirePermission(Permission.USER_MANAGE)
  fun patchPosition(
    @RequestBody dto: PhotoGangBangerPositionPatchRequestDto,
  ): PhotoGangBangerDto? = repository.patchPosition(dto)
}

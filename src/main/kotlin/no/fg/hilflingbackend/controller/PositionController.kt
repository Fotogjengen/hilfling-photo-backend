package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.configurations.RequirePermission
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.PositionDto
import no.fg.hilflingbackend.dto.PositionPatchRequestDto
import no.fg.hilflingbackend.service.PositionService
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
@RequestMapping("/positions")
class PositionController(
  val positionService: PositionService,
) {
  @GetMapping("/{id}")
  fun getById(
    @PathVariable("id") id: UUID,
  ): PositionDto = positionService.findById(id)

  @GetMapping
  fun getAll(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
  ): Page<PositionDto> = positionService.findAll(page ?: 0, pageSize ?: 100)

  @PostMapping
  @RequirePermission(Permission.POSITION_MANAGE)
  fun create(
    @RequestBody dto: PositionDto,
  ): Int = positionService.create(dto)

  @DeleteMapping("/{id}")
  @RequirePermission(Permission.POSITION_MANAGE)
  fun delete(
    @PathVariable("id") id: UUID,
  ): Int = positionService.delete(id)

  @PatchMapping
  @RequirePermission(Permission.POSITION_MANAGE)
  fun patch(
    @RequestBody dto: PositionPatchRequestDto,
  ): PositionDto = positionService.patch(dto)
}

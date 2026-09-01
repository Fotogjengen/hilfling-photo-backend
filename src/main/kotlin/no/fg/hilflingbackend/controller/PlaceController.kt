package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.configurations.RequirePermission
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.PlaceDto
import no.fg.hilflingbackend.dto.PlacePatchRequestDto
import no.fg.hilflingbackend.service.PlaceService
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
@RequestMapping("/places")
class PlaceController(
  val placeService: PlaceService,
) {
  @GetMapping("/{id}")
  fun getById(
    @PathVariable("id") id: UUID,
  ): PlaceDto = placeService.findById(id)

  @GetMapping
  fun getAll(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
  ): Page<PlaceDto> = placeService.findAll(page ?: 0, pageSize ?: 100)

  @PostMapping
  @RequirePermission(Permission.ARCHIVE_MANAGE)
  fun create(
    @RequestBody dto: PlaceDto,
  ): Int = placeService.create(dto)

  @DeleteMapping("/{id}")
  @RequirePermission(Permission.ARCHIVE_MANAGE)
  fun delete(
    @PathVariable("id") id: UUID,
  ): Int = placeService.delete(id)

  @PatchMapping
  @RequirePermission(Permission.ARCHIVE_MANAGE)
  fun patch(
    @RequestBody dto: PlacePatchRequestDto,
  ): PlaceDto = placeService.patch(dto)
}

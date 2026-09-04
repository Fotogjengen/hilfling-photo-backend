package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.configurations.RequirePermission
import no.fg.hilflingbackend.dto.EventOwnerDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.service.EventOwnerService
import no.fg.hilflingbackend.valueobject.Permission
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
@RequestMapping("/event_owners")
class EventOwnerController(
  val eventOwnerService: EventOwnerService,
) {
  @GetMapping("/{id}")
  fun getById(
    @PathVariable("id") id: UUID,
  ): EventOwnerDto = eventOwnerService.findById(id)

  @GetMapping
  fun getAll(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
  ): Page<EventOwnerDto> = eventOwnerService.findAll(page ?: 0, pageSize ?: 100)

  @PostMapping
  @RequirePermission(Permission.ARCHIVE_MANAGE)
  fun create(
    @RequestBody dto: EventOwnerDto,
  ): Int = eventOwnerService.create(dto)

  @DeleteMapping("/{id}")
  @RequirePermission(Permission.ARCHIVE_MANAGE)
  fun delete(
    @PathVariable("id") id: UUID,
  ): Int = eventOwnerService.delete(id)
}

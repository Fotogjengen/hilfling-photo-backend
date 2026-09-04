package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.configurations.RequirePermission
import no.fg.hilflingbackend.dto.AlbumDto
import no.fg.hilflingbackend.dto.AlbumPatchRequestDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.service.AlbumService
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
@RequestMapping("/albums")
class AlbumController(
  val albumService: AlbumService,
) {
  @GetMapping("/{id}")
  fun getById(
    @PathVariable("id") id: UUID,
  ): AlbumDto = albumService.findById(id)

  @GetMapping
  fun getAll(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
  ): Page<AlbumDto> = albumService.findAll(page ?: 0, pageSize ?: 100)

  @PostMapping
  @RequirePermission(Permission.ALBUM_MANAGE)
  fun create(
    @RequestBody dto: AlbumDto,
  ): Int = albumService.create(dto)

  @DeleteMapping("/{id}")
  @RequirePermission(Permission.ALBUM_MANAGE)
  fun delete(
    @PathVariable("id") id: UUID,
  ): Int = albumService.delete(id)

  @PatchMapping
  @RequirePermission(Permission.ALBUM_MANAGE)
  fun patch(
    @RequestBody dto: AlbumPatchRequestDto,
  ): AlbumDto = albumService.patch(dto)

  @GetMapping("/analog")
  fun getAnalog(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
  ): Page<AlbumDto> = albumService.findAllAnalog(page ?: 0, pageSize ?: 100)

  @GetMapping("/digital")
  fun getDigital(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
  ): Page<AlbumDto> = albumService.findAllDigital(page ?: 0, pageSize ?: 100)
}

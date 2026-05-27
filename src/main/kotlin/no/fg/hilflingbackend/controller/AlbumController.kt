package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.AlbumDto
import no.fg.hilflingbackend.dto.AlbumPatchRequestDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.model.Album
import no.fg.hilflingbackend.repository.AlbumRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/albums")
class AlbumController(
  override val repository: AlbumRepository,
) : BaseController<Album, AlbumDto, AlbumPatchRequestDto>(repository) {
  @PostMapping
  override fun create(
    @RequestBody dto: AlbumDto,
  ): Int =
    repository.create(
      dto,
    )

  @GetMapping("/analog")
  fun getAnalog(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
  ): Page<AlbumDto> = repository.findAllAnalog(page ?: 0, pageSize ?: 100)

  @GetMapping("/digital")
  fun getDigital(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
  ): Page<AlbumDto> = repository.findAllDigital(page ?: 0, pageSize ?: 100)
}

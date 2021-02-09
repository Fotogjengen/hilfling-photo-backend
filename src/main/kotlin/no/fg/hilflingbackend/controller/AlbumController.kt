package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.AlbumDto
import no.fg.hilflingbackend.model.Album
import no.fg.hilflingbackend.repository.AlbumRepository
import no.fg.hilflingbackend.service.AlbumService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/albums")
class AlbumController(override val repository: AlbumRepository, val service: AlbumService) : BaseController<Album, AlbumDto>(repository) {
  @PostMapping
  override fun create(
    @RequestBody dto: AlbumDto
  ): Int {
    val directoryPath = service.createDirectory(dto.title)
    println(directoryPath)
    return repository.create(
      dto
    )
  }
}

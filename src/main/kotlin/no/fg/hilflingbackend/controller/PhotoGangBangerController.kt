package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.PhotoGangBangerDto
import no.fg.hilflingbackend.repository.PhotoGangBangerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/photo_gang_bangers")
class PhotoGangBangerController {
  // TODO: Refactor to use BaseController
  @Autowired
  lateinit var repository: PhotoGangBangerRepository

  @GetMapping("/{id}")
  fun getById(@PathVariable("id") id: UUID): PhotoGangBangerDto? {
    return repository.findById(id)
  }

  @GetMapping
  fun getAll(): List<PhotoGangBangerDto> {
    return repository.findAll()
  }

  @GetMapping("/actives")
  fun getActives(): List<PhotoGangBangerDto> {
    return repository.findAllActives()
  }

  @GetMapping("/active_pangs")
  fun getActivePangs(): List<PhotoGangBangerDto> {
    return repository.findAllActivePangs()
  }

  @GetMapping("/inactive_pangs")
  fun getInActivePangs(): List<PhotoGangBangerDto> {
    return repository.findAllInActivePangs()
  }

  @PostMapping
  fun create(
    @RequestBody photoGangBangerDto: PhotoGangBangerDto
  ): Int =
    repository.create(photoGangBangerDto)
}

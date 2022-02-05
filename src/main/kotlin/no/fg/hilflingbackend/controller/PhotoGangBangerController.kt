package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.PhotoGangBangerDto
import no.fg.hilflingbackend.dto.SemesterStart
import no.fg.hilflingbackend.repository.PhotoGangBangerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
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

  @PostMapping("/create")
  fun create(
    @RequestBody dto: PhotoGangBangerDto
  ): Int =
    repository.create(dto)

  @PostMapping
  fun create2(
    @RequestBody dto: SemesterStart
  ): Int {
    return 1
  }

  @PatchMapping()
  fun patch(
    @RequestBody dto: PhotoGangBangerDto
  ): Int {
    println(dto)
    return repository.patch(dto);
  }
}

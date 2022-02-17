package no.fg.hilflingbackend.controller

import com.azure.core.models.ResponseError
import hilfling.backend.hilfling.exceptions.RestExceptionHandler
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.PhotoGangBangerDto
import no.fg.hilflingbackend.dto.PhotoGangBangerPatchRequestDto
import no.fg.hilflingbackend.dto.SamfundetUserDto
import no.fg.hilflingbackend.exceptions.EntityCreationException
import no.fg.hilflingbackend.exceptions.GlobalExceptionHandler
import no.fg.hilflingbackend.repository.PhotoGangBangerRepository
import no.fg.hilflingbackend.repository.SamfundetUserRepository
import no.fg.hilflingbackend.utils.ResponseCreated
import no.fg.hilflingbackend.utils.ResponseNotCreated
import no.fg.hilflingbackend.utils.ResponseOk
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.UUID

@RestController
@RequestMapping("/photo_gang_bangers")
class PhotoGangBangerController(
  val repository: PhotoGangBangerRepository,
  val samfundetUserRepository: SamfundetUserRepository
) : RestExceptionHandler() {

  @GetMapping("/{id}")
  fun getById(@PathVariable("id") id: UUID): PhotoGangBangerDto? {
    return repository.findById(id)
  }

  @GetMapping
  fun getAll(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?
  ): Page<PhotoGangBangerDto> {
    return repository.findAll(page = 0, pageSize = 100)
  }

  @GetMapping("/actives")
  fun getActives(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?
  ): Page<PhotoGangBangerDto> {
    return repository.findAllActives(page = 0, pageSize = 100)
  }

  @GetMapping("/active_pangs")
  fun getActivePangs(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?
  ): Page<PhotoGangBangerDto> {
    return repository.findAllActivePangs(page = 0, pageSize = 100)
  }

  @GetMapping("/inactive_pangs")
  fun getInActivePangs(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?
  ): Page<PhotoGangBangerDto> {
    return repository.findAllInactivePangs(page = 0, pageSize = 100)
  }

  @PostMapping
  fun create(
    @RequestBody dto: PhotoGangBangerDto
  ): ResponseEntity<Int> {
    val created = repository.create(dto)
    return ResponseCreated(created)
  }

  @PatchMapping()
  fun patch(
    @RequestBody dto: PhotoGangBangerPatchRequestDto
  ): PhotoGangBangerDto? {
    return repository.patch(dto)
  }
}

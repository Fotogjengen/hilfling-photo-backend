package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.MotiveCreateRequestDto
import no.fg.hilflingbackend.dto.MotiveDefaultsDto
import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.dto.MotivePatchRequestDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.repository.MotiveRepository
import no.fg.hilflingbackend.service.MotiveService
import org.springframework.beans.factory.annotation.Autowired
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
@RequestMapping("/motives")
class MotiveController {
  @Autowired
  lateinit var repository: MotiveRepository

  @Autowired
  lateinit var motiveService: MotiveService

  @GetMapping("/{id}")
  fun getById(
    @PathVariable("id") id: UUID,
  ): MotiveDto? = repository.findById(id)

  @GetMapping
  fun getAll(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
  ): Page<MotiveDto> = repository.findAll(page ?: 0, pageSize ?: 10)

  @GetMapping("/search/{searchTerm}")
  fun search(
    @PathVariable("searchTerm") searchTerm: String,
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
  ): Page<MotiveDto> = repository.search(searchTerm, page ?: 0, pageSize ?: 10)

  @PostMapping
  fun create(
    @RequestBody dto: MotiveCreateRequestDto,
  ): MotiveDto = repository.create(dto)

  @PatchMapping
  fun patch(
    @RequestBody dto: MotivePatchRequestDto,
  ): MotiveDto = repository.patch(dto)

  @DeleteMapping("/{id}")
  fun delete(
    @PathVariable("id") id: UUID,
  ): Int = repository.delete(id)

  @GetMapping("/defaults")
  fun getDefaults(): MotiveDefaultsDto = motiveService.getMotiveDefaults()

}

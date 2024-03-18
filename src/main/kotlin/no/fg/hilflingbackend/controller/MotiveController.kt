package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.dto.MotivePatchRequestDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.repository.MotiveRepository
import org.springframework.beans.factory.annotation.Autowired
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

  @GetMapping("/{id}")
  fun getById(@PathVariable("id") id: UUID): MotiveDto? {
    return repository.findById(id)
  }

  @GetMapping
  fun getAll(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?
  ): Page<MotiveDto> {
    return repository.findAll(page ?: 0, pageSize ?: 100)
  }

  @GetMapping("/getUuidByTitle")
  fun getAlbumIdByName(
    @RequestParam(
      "motiveTitle",
    ) motiveTitle: String,
  ): UUID? {
    return repository.findUuidByMotive(motiveTitle)
  }

  @PostMapping
  fun create(
    @RequestBody dto: MotiveDto
  ): MotiveDto {
    return repository.create(
      dto
    )
  }

  @PatchMapping
  fun patch(
    @RequestBody dto: MotivePatchRequestDto
  ): MotiveDto {
    return repository.patch(dto)
  }
}

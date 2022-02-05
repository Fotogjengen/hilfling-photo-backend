package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.model.Motive
import no.fg.hilflingbackend.model.toDto
import no.fg.hilflingbackend.repository.MotiveRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
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
    @RequestParam("offset", required = false) offset: Int?,
    @RequestParam("limit", required = false) limit: Int?
  ): Page<MotiveDto> {
    return repository.findAll(offset ?: 0, limit ?: 100)
  }

  @PostMapping
  fun create(
    @RequestBody motive: Motive
  ): MotiveDto {
    return repository.create(
      motive.toDto()
    )
  }
}

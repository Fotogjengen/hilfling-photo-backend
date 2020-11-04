package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.model.Gang
import no.fg.hilflingbackend.repository.GangRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/gangs")
class GangController {
  @Autowired
  lateinit var repository: GangRepository

  @GetMapping("/{id}")
  fun getById(@PathVariable("id") id: UUID): Gang? {
    return repository.findById(id)
  }

  @GetMapping
  fun getAll(): List<Gang> {
    return repository.findAll()
  }

  @PostMapping
  fun create(
    @RequestBody gang: Gang
  ): Int =
    repository.create(
      gang
    )
}

package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.model.Motive
import no.fg.hilflingbackend.repository.MotiveRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/motives")
class MotiveController {
  @Autowired
  lateinit var repository: MotiveRepository

  @GetMapping("/{id}")
  fun getById(@PathVariable("id") id: UUID): Motive? {
    return repository.findById(id)
  }

  @GetMapping
  fun getAll(): List<Motive> {
    return repository.findAll()
  }

  @PostMapping
  fun create(
    @RequestBody motive: Motive
  ): Int {
    return repository.create(
      motive
    )
  }
}

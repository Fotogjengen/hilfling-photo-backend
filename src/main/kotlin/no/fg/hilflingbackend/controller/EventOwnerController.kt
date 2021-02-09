package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.model.EventOwner
import no.fg.hilflingbackend.repository.EventOwnerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/event_owners")
class EventOwnerController {
  @Autowired
  lateinit var repository: EventOwnerRepository

  @GetMapping("/{id}")
  fun getById(@PathVariable("id") id: UUID): EventOwner? {
    return repository.findById(id)
  }

  @GetMapping
  fun getAll(): List<EventOwner> {
    return repository.findAll()
  }

  @PostMapping
  fun create(
    @RequestBody eventOwner: EventOwner
  ): Int {
    return repository.create(
      eventOwner
    )
  }
}

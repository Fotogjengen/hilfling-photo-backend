package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.model.EventOwner
import no.fg.hilflingbackend.repository.EventOwnerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/event_owners")
class EventOwnerController {
    @Autowired
    lateinit var repository: EventOwnerRepository

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Int): EventOwner? {
        return repository.findById(id)
    }

    @GetMapping
    fun getAll(): List<EventOwner> {
        return repository.findAll()
    }

    @PostMapping
    fun create(
            @RequestBody eventOwner: EventOwner
    ): EventOwner {
        return repository.create(
                eventOwner
        )
    }
}
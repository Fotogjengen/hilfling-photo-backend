package hilfling.backend.hilfling.controller

import hilfling.backend.hilfling.model.*
import hilfling.backend.hilfling.repository.EventOwnerRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/event_owners")
class EventOwnerController {
    val repository = EventOwnerRepository()

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
            @RequestParam("eventOwner") eventOwner: EventOwner
    ): EventOwner {
        return repository.create(
                eventOwner
        )
    }
}
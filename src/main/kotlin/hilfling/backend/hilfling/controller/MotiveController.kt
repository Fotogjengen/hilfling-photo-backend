package hilfling.backend.hilfling.controller

import hilfling.backend.hilfling.model.Album
import hilfling.backend.hilfling.model.Category
import hilfling.backend.hilfling.model.EventOwner
import hilfling.backend.hilfling.model.Motive
import hilfling.backend.hilfling.repository.MotiveRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/motives")
class MotiveController {
    val repository = MotiveRepository()

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Int): Motive? {
        return repository.findById(id)
    }

    @GetMapping
    fun getAll(): List<Motive> {
        return repository.findAll()
    }

    @PostMapping
    fun create(
            @RequestParam("title") title: String,
            @RequestParam("category") category: Category,
            @RequestParam("eventOwner") eventOwner: EventOwner,
            @RequestParam("album") album: Album
    ): Motive {
        return repository.create(
                title,
                category,
                eventOwner,
                album
        )
    }
}
package hilfling.backend.hilfling.controller

import hilfling.backend.hilfling.model.*
import hilfling.backend.hilfling.repository.AlbumRepository
import hilfling.backend.hilfling.repository.ArticleRepository
import hilfling.backend.hilfling.repository.CategoryRepository
import hilfling.backend.hilfling.repository.EventOwnerRepository
import me.liuwj.ktorm.database.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/event_owners")
class EventOwnerController {
    val repository = EventOwnerRepository()

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Long): EventOwner? {
        return repository.findById(id)
    }

    @GetMapping
    fun getAll(): List<EventOwner> {
        return repository.findAll()
    }

    @PostMapping
    fun create(
            @RequestParam("name") name: String
    ): EventOwner {
        return repository.create(
                name
        )
    }
}
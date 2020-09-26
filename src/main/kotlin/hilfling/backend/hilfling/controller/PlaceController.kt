package hilfling.backend.hilfling.controller

import hilfling.backend.hilfling.model.Place
import hilfling.backend.hilfling.repository.PlaceRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/places")
class PlaceController {
    val repository = PlaceRepository()

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Int) : Place? {
        return repository.findById(id)
    }

    @GetMapping
    fun getAll() : List<Place> {
        return repository.findAll()
    }

    @PostMapping
    fun create(
            @RequestParam("name") name: String
    ): Place {
        return repository.create(name)
    }
}
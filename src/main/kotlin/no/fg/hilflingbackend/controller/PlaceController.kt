package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.model.Place
import no.fg.hilflingbackend.repository.PlaceRepository
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
            @RequestParam("place") place: Place
    ): Place {
        return repository.create(place)
    }
}
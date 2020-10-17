package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.model.Place
import no.fg.hilflingbackend.repository.PlaceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/places")
class PlaceController {
  @Autowired
  lateinit var repository: PlaceRepository

  @GetMapping("/{id}")
  fun getById(@PathVariable("id") id: Int): Place? {
    return repository.findById(id)
  }

  @GetMapping
  fun getAll(): List<Place> {
    return repository.findAll()
  }

  @PostMapping
  fun create(
    @RequestBody place: Place
  ): Place {
    return repository.create(place)
  }
}

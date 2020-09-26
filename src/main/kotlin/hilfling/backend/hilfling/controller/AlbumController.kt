package hilfling.backend.hilfling.controller

import hilfling.backend.hilfling.model.Album
import hilfling.backend.hilfling.repository.AlbumRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/albums")
class AlbumController {
    val repository = AlbumRepository()

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Int) : Album? {
        return repository.findById(id)
    }

    @GetMapping
    fun getAll() : List<Album> {
        return repository.findAll()
    }

    @PostMapping
    fun create(
            @RequestParam("title") title: String,
            @RequestParam("isAnalog") isAnalog: Boolean
    ): Album {
        return repository.create(title, isAnalog)
    }
}
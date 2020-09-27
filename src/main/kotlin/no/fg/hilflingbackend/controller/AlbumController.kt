package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.model.Album
import no.fg.hilflingbackend.repository.AlbumRepository
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
            @RequestParam("album") album: Album
    ): Album {
        return repository.create(album)
    }
}
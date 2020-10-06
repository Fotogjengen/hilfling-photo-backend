package no.fg.hilflingbackend.controller

import me.liuwj.ktorm.database.Database
import no.fg.hilflingbackend.model.Album
import no.fg.hilflingbackend.repository.AlbumRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/albums")
class AlbumController {
    @Autowired
    lateinit var repository: AlbumRepository

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
            @RequestBody album: Album
    ): Album {
        return repository.create(album)
    }
}
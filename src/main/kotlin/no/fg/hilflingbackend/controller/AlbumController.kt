package no.fg.hilflingbackend.controller

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.model.Album
import no.fg.hilflingbackend.model.albums
import no.fg.hilflingbackend.repository.AlbumRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/albums")
class AlbumController {

    /*@Autowired
    lateinit var repository: AlbumRepository*/

    @Autowired
    lateinit var database: Database

    val repository = AlbumRepository()

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Int) : Album? {
        return repository.findById(id)
    }

    @GetMapping
    fun getAll() : List<Album> {
        return database.albums.toList()
        //return repository.findAll()
    }

    @PostMapping
    fun create(
            @RequestParam("album") album: Album
    ): Album {
        return repository.create(album)
    }
}
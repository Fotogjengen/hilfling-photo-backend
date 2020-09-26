package hilfling.backend.hilfing.controller

import hilfling.backend.hilfing.model.Album
import hilfling.backend.hilfling.repository.AlbumRepository
import me.liuwj.ktorm.database.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/albums")
class AlbumController {
    val repository = AlbumRepository()

    @GetMapping("/{id}")
    fun getAlbumById(@PathVariable("id") id: Long) : Album? {
        return repository.findById(id)
    }

    @GetMapping
    fun getAllAlbums() : List<Album> {
        return repository.findAll()
    }

    @PostMapping
    fun createAlbum(
            @RequestParam("title") title: String,
            @RequestParam("isAnalog") isAnalog: Boolean
    ): Album {
        return repository.create(title, isAnalog)
    }
}
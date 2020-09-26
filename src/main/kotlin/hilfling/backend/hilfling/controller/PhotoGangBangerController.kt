package hilfling.backend.hilfling.controller

import hilfling.backend.hilfling.model.Album
import hilfling.backend.hilfling.model.PhotoGangBanger
import hilfling.backend.hilfling.repository.AlbumRepository
import hilfling.backend.hilfling.repository.PhotoGangBangerRepository
import me.liuwj.ktorm.database.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/photo_gang_bangers")
class PhotoGangBangerController {
    val repository = PhotoGangBangerRepository()

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Long): PhotoGangBanger? {
        return repository.findById(id)
    }

    @GetMapping
    fun getAll(): List<PhotoGangBanger> {
        return repository.findAll()
    }

    @GetMapping("/actives")
    fun getActives(): List<PhotoGangBanger> {
        return repository.findAllActives()
    }

    @GetMapping("/active_pangs")
    fun getActivePangs(): List<PhotoGangBanger> {
        return repository.findAllActivePangs()
    }

    @GetMapping("/inactive_pangs")
    fun getInActivePangs(): List<PhotoGangBanger> {
        return repository.findAllInActivePangs()
    }

    @PostMapping
    fun create(
            @RequestParam("photoGangBanger") photoGangBanger: PhotoGangBanger
    ): PhotoGangBanger {
        return repository.create(photoGangBanger)
    }
}
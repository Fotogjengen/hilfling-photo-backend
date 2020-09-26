package hilfling.backend.hilfling.controller

import hilfling.backend.hilfling.model.Album
import hilfling.backend.hilfling.model.Position
import hilfling.backend.hilfling.repository.AlbumRepository
import hilfling.backend.hilfling.repository.PositionRepository
import me.liuwj.ktorm.database.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/positions")
class PositionController {
    val repository = PositionRepository()

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Int) : Position? {
        return repository.findById(id)
    }

    @GetMapping
    fun getAll() : List<Position> {
        return repository.findAll()
    }

    @PostMapping
    fun create(
            @RequestParam("position") position: Position
    ): Position {
        return repository.create(position)
    }
}
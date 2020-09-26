package hilfling.backend.hilfling.controller

import hilfling.backend.hilfling.model.Position
import hilfling.backend.hilfling.repository.PositionRepository
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
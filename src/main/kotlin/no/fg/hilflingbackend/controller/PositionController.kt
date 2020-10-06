
package no.fg.hilflingbackend.controller
import no.fg.hilflingbackend.model.Position
import no.fg.hilflingbackend.repository.PositionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/positions")
class PositionController {
    @Autowired
    lateinit var  repository: PositionRepository

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
            @RequestBody position: Position
    ): Position {
        return repository.create(position)
    }
}
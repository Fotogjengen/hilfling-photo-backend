package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.model.Motive
import no.fg.hilflingbackend.repository.MotiveRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/motives")
class MotiveController {
    val repository = MotiveRepository()

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Int): Motive? {
        return repository.findById(id)
    }

    @GetMapping
    fun getAll(): List<Motive> {
        return repository.findAll()
    }

    @PostMapping
    fun create(
            @RequestParam("motive") motive: Motive
    ): Motive {
        return repository.create(
                motive
        )
    }
}
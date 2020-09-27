package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.model.Gang
import no.fg.hilflingbackend.repository.GangRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/gangs")
class GangController {
    val repository = GangRepository()

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Int): Gang? {
        return repository.findById(id)
    }

    @GetMapping
    fun getAll(): List<Gang> {
        return repository.findAll()
    }

    @PostMapping
    fun create(
            @RequestParam("gang") gang: Gang
    ): Gang {
        return repository.create(
                gang
        )
    }
}
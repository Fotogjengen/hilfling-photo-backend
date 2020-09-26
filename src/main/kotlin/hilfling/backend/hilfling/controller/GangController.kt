package hilfling.backend.hilfling.controller

import hilfling.backend.hilfling.model.*
import hilfling.backend.hilfling.repository.*
import me.liuwj.ktorm.database.Database
import org.springframework.beans.factory.annotation.Autowired
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
            @RequestParam("name") name: String
    ): Gang {
        return repository.create(
                name
        )
    }
}
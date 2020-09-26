package hilfling.backend.hilfling.controller

import hilfling.backend.hilfling.model.*
import hilfling.backend.hilfling.repository.AlbumRepository
import hilfling.backend.hilfling.repository.ArticleRepository
import hilfling.backend.hilfling.repository.CategoryRepository
import me.liuwj.ktorm.database.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/categories")
class CategoryController {
    val repository = CategoryRepository()

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Long): Category? {
        return repository.findById(id)
    }

    @GetMapping
    fun getAll(): List<Category> {
        return repository.findAll()
    }

    @PostMapping
    fun create(
            @RequestParam("name") name: String
    ): Category {
        return repository.create(
                name
        )
    }
}
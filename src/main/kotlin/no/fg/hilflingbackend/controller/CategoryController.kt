package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.model.Category
import no.fg.hilflingbackend.repository.CategoryRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/categories")
class CategoryController {
    val repository = CategoryRepository()

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Int): Category? {
        return repository.findById(id)
    }

    @GetMapping
    fun getAll(): List<Category> {
        return repository.findAll()
    }

    @PostMapping
    fun create(
            @RequestParam("category") category: Category
    ): Category {
        return repository.create(
                category
        )
    }
}
package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.model.Category
import no.fg.hilflingbackend.repository.CategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/categories")
class CategoryController {
  @Autowired
  lateinit var repository: CategoryRepository

  @GetMapping("/{id}")
  fun getById(@PathVariable("id") id: UUID): Category? {
    return repository.findById(id)
  }

  @GetMapping
  fun getAll(): List<Category> {
    return repository.findAll()
  }

  @PostMapping
  fun create(
    @RequestBody category: Category
  ): Category {
    return repository.create(
      category
    )
  }
}

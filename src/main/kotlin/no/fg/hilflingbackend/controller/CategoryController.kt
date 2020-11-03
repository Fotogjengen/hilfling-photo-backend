package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.CategoryDto
import no.fg.hilflingbackend.model.Category
import no.fg.hilflingbackend.repository.CategoryRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/categories")
open class CategoryController(override val repository: CategoryRepository) : BaseController<Category, CategoryDto>(repository) {
}

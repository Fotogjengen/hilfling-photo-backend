package hilfling.backend.hilfling.controller

import hilfling.backend.hilfling.model.Article
import hilfling.backend.hilfling.model.PhotoGangBanger
import hilfling.backend.hilfling.model.SecurityLevel
import hilfling.backend.hilfling.repository.ArticleRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/articles")
class ArticleController {
    val repository = ArticleRepository()

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Int): Article? {
        return repository.findById(id)
    }

    @GetMapping
    fun getAll(): List<Article> {
        return repository.findAll()
    }

    @PostMapping
    fun create(
            @RequestParam("title") title: String,
            @RequestParam("plainTextBody") plainTextBody: String,
            @RequestParam("securityLevel") securityLevel: SecurityLevel,
            @RequestParam("photoGangBanger") photoGangBanger: PhotoGangBanger
    ): Article {
        return repository.create(
                title,
                plainTextBody,
                securityLevel,
                photoGangBanger
        )
    }
}
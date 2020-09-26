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
            @RequestParam("article") article: Article
    ): Article {
        return repository.create(
                article
        )
    }
}
package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.model.Article
import no.fg.hilflingbackend.repository.ArticleRepository
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
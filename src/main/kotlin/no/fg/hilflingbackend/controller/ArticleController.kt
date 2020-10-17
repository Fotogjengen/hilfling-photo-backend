package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.model.Article
import no.fg.hilflingbackend.repository.ArticleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/articles")
class ArticleController {
  @Autowired
  lateinit var repository: ArticleRepository

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
    @RequestBody article: Article
  ): Article {
    return repository.create(
      article
    )
  }
}

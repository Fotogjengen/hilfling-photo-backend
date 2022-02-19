package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.ArticleDto
import no.fg.hilflingbackend.model.toDto
import no.fg.hilflingbackend.repository.ArticleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/articles")
class ArticleController {
  @Autowired
  lateinit var repository: ArticleRepository

  @GetMapping("/{id}")
  fun getById(@PathVariable("id") id: UUID): ArticleDto {
    return repository.findById(id).toDto()
  }

  @GetMapping
  fun getAll(): List<ArticleDto> {
    return repository.findAll()
  }

  @PostMapping
  fun create(
    @RequestBody article: ArticleDto
  ): Int {
    return repository.create(
      article
    )
  }
}

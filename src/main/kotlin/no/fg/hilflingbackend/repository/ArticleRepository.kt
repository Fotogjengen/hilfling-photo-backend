package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.dto.ArticleDto
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.Article
import no.fg.hilflingbackend.model.articles
import no.fg.hilflingbackend.model.toDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.UUID
import javax.persistence.EntityNotFoundException

@Repository
open class ArticleRepository {
  @Autowired
  open lateinit var database: Database

  fun findById(id: UUID): Article {
    return database.articles.find { it.id eq id }
      ?: throw EntityNotFoundException("Did not find Article")
  }

  fun findAll(): List<ArticleDto> {
    return database.articles.toList().map { it.toDto() }
  }

  fun create(
    dto: ArticleDto
  ): Int {
    return database.articles.add(dto.toEntity())
  }
}

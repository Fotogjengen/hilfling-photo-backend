package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.dto.ArticleDto
import no.fg.hilflingbackend.dto.ArticlePatchRequestDto
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.articles
import no.fg.hilflingbackend.model.toDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.UUID
import jakarta.persistence.EntityNotFoundException

@Repository
open class ArticleRepository {
  @Autowired
  open lateinit var database: Database

  fun findById(id: UUID): ArticleDto {
    val article = database.articles.find { it.id eq id }
      ?: throw EntityNotFoundException("Did not find Article")
    return article.toDto()
  }

  fun findAll(): List<ArticleDto> {
    return database.articles.toList().map { it.toDto() }
  }

  fun create(
    dto: ArticleDto
  ): Int {
    return database.articles.add(dto.toEntity())
  }

  fun patch(
    dto: ArticlePatchRequestDto
  ): ArticleDto {
    val fromDb = findById(dto.articleId.id)
    val newDto = ArticleDto(
      articleId = fromDb.articleId,
      title = dto.title ?: fromDb.title,
      plainText = dto.plainText ?: fromDb.plainText,
      securityLevel = dto.securityLevel ?: fromDb.securityLevel,
      photoGangBanger = fromDb.photoGangBanger
    )
    val updated = database.articles.update(newDto.toEntity())

    return if (updated == 1) newDto else fromDb
  }
}

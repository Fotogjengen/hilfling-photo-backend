package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.dto.ArticleTagDto
import no.fg.hilflingbackend.dto.ArticleTagId
import no.fg.hilflingbackend.dto.ArticleTagPatchRequestDto
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.ArticleTag
import no.fg.hilflingbackend.model.ArticleTags
import no.fg.hilflingbackend.model.article_tags
import org.springframework.stereotype.Repository
import javax.persistence.EntityNotFoundException

@Repository
open class ArticleTagRepository(database: Database) :
  BaseRepository<ArticleTag, ArticleTagDto, ArticleTagPatchRequestDto>(table = ArticleTags, database = database) {
  override fun convertToClass(qrs: QueryRowSet): ArticleTagDto = ArticleTagDto(
    articleTagId = ArticleTagId(qrs[ArticleTags.id]!!),
    name = qrs[ArticleTags.name]!!,
  )

  override fun create(dto: ArticleTagDto): Int {
    return database.article_tags.add(dto.toEntity())
  }

  override fun patch(dto: ArticleTagPatchRequestDto): ArticleTagDto {
    val fromDb = findById(dto.articleTagId.id)
      ?: throw EntityNotFoundException("Could not find ArticleTag")
    val newDto = ArticleTagDto(
      articleTagId = fromDb.articleTagId,
      name = dto.name ?: fromDb.name
    )
    val updated = database.article_tags.update(newDto.toEntity())

    return if (updated == 1) newDto else fromDb
  }
}

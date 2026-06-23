package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.Article
import no.fg.hilflingbackend.model.toDto
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import java.util.UUID

data class ArticlePatchRequestDto(
  val articleId: ArticleId,
  val title: String?,
  val plainText: String?,
  val securityLevel: SecurityLevelDto?,
)

data class ArticleDto(
  val articleId: ArticleId = ArticleId(),
  val title: String,
  val plainText: String,
  val securityLevel: SecurityLevelDto,
  val photoGangBanger: PhotoGangBangerDto,
)

data class ArticleId(
  override val id: UUID = UUID.randomUUID(),
) : UuidId {
  override fun toString(): String = id.toString()
}

fun Article.toDto(): ArticleDto =
  ArticleDto(
    articleId = ArticleId(this.id),
    title = this.title,
    plainText = this.plainText,
    securityLevel = SecurityLevelDto(SecurityLevelType.valueOf(this.securityLevel)),
    photoGangBanger = this.photoGangBanger.toDto(),
  )

fun ArticleDto.toEntity(): Article {
  val dto = this
  return Article {
    id = dto.articleId.id
    title = dto.title
    plainText = dto.plainText
    securityLevel = dto.securityLevel.securityLevelType.type
    photoGangBanger = dto.photoGangBanger.toEntity()
  }
}

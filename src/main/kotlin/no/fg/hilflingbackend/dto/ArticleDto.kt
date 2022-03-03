package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.Article
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
  val photoGangBanger: PhotoGangBangerDto
)

data class ArticleId(
  override val id: UUID = UUID.randomUUID()
) : UuidId {
  override fun toString(): String = id.toString()
}

fun ArticleDto.toEntity(): Article {
  val dto = this
  return Article {
    id = dto.articleId.id
    title = dto.title
    plainText = dto.plainText
    securityLevel = dto.securityLevel.toEntity()
    photoGangBanger = dto.photoGangBanger.toEntity()
  }
}

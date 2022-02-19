package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.ArticleTag
import java.util.*

data class ArticleTagPatchRequestDto(
  val articleTagId: ArticleTagId,
  val name: String?
)

data class ArticleTagDto (
  val articleTagId: ArticleTagId = ArticleTagId(),
  val name: String
)

data class ArticleTagId(
  override val id: UUID = UUID.randomUUID()
): UuidId {
  override fun toString(): String = id.toString()
}

fun ArticleTagDto.toEntity(): ArticleTag {
  val dto = this
  return ArticleTag{
    id = dto.articleTagId.id
    name = dto.name
  }
}

package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar
import no.fg.hilflingbackend.dto.ArticleDto
import no.fg.hilflingbackend.dto.ArticleId
import no.fg.hilflingbackend.dto.toDto

interface Article : BaseModel<Article> {
  companion object : Entity.Factory<Article>()

  var title: String
  var plainText: String

  // Foreign keys
  var securityLevel: SecurityLevel
  var photoGangBanger: PhotoGangBanger
}

object Articles : BaseTable<Article>("article") {
  val title = varchar("title").bindTo { it.title }
  val plainText = varchar("plain_text").bindTo { it.plainText }

  // Foreign keys
  val securityLevelId = int("security_level_id").references(SecurityLevels) { it.securityLevel }
  val photoGangBangerId = int("photo_gang_banger_id").references(PhotoGangBangers) { it.photoGangBanger }
}

val Database.articles get() = this.sequenceOf(Articles)

fun Article.toDto(): ArticleDto = ArticleDto(
  articleId = ArticleId(this.id),
  title = this.title,
  plainText = this.plainText,
  securityLevel = this.securityLevel.toDto(),
  photoGangBanger = this.photoGangBanger.toDto()
)

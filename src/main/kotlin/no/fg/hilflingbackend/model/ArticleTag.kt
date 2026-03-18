package no.fg.hilflingbackend.model

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.varchar

interface ArticleTag : BaseModel<ArticleTag> {
  companion object : Entity.Factory<ArticleTag>()

  var name: String
}

object ArticleTags : BaseTable<ArticleTag>("article_tag") {
  val name = varchar("name").bindTo { it.name }
}

val Database.article_tags get() = this.sequenceOf(ArticleTags)

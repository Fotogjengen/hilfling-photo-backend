package hilfling.backend.hilfling.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.*

interface ArticleTag : BaseModel<ArticleTag> {
    companion object : Entity.Factory<ArticleTag>()

    var name: String
}

object ArticleTags : BaseTable<ArticleTag>("article_tag") {
    val name = varchar("name").bindTo { it.name }
}

val Database.article_tags get() = this.sequenceOf(ArticleTags)
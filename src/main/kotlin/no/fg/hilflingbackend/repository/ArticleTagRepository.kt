package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.model.ArticleTag
import no.fg.hilflingbackend.model.article_tags
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class ArticleTagRepository {
    @Autowired
    open lateinit var database: Database

    fun findById(id: Int): ArticleTag? {
        return database.article_tags.find { it.id eq id }
    }

    fun findAll(): List<ArticleTag> {
        return database.article_tags.toList()
    }

    fun create(
            articleTag: ArticleTag
    ): ArticleTag {
        val articleTagFromDatabase = ArticleTag{
            this.name = articleTag.name
        }
        database.article_tags.add(articleTagFromDatabase)
        return articleTagFromDatabase
    }
}
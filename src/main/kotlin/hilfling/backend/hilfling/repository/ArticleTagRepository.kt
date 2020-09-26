package hilfling.backend.hilfling.repository

import hilfling.backend.hilfing.model.*
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.from
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.where
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate

class ArticleTagRepository {
    @Autowired
    lateinit var database: Database

    fun findById(id: Long): ArticleTag? {
        return database.article_tags.find { it.id eq id }
    }

    fun findAll(): List<ArticleTag> {
        return database.article_tags.toList()
    }

    fun create(name: String): ArticleTag {
        val articleTag = ArticleTag{
            this.name = name
        }
        database.article_tags.add(articleTag)
        return articleTag
    }
}
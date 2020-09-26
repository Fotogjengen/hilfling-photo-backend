package hilfling.backend.hilfing.repository

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

class ArticleRepository {
    @Autowired
    lateinit var database: Database

    fun findById(id: Long): Article? {
        return database.articles.find { it.id eq id }
    }

    fun findAll(): List<Article> {
        return database.articles.toList()
    }

    fun create(
            title: String,
            plainTextBody: String,
            securityLevelId: Long,
            photoGangBangerId: Long
    ): Article? {
        val securityLevel = database.security_levels.find { it.id eq securityLevelId } ?: return null
        val photoGangBanger = database.photo_gang_bangers.find { it.id eq photoGangBangerId } ?: return null

        val article = Article{
            this.title = title
            this.plainTextBody = plainTextBody

            this.securityLevel = securityLevel
            this.photoGangBanger = photoGangBanger
        }
        database.articles.add(article)
        return article
    }
}
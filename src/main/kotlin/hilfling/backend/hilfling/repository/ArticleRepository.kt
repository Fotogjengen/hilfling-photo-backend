package hilfling.backend.hilfing.repository

import hilfling.backend.hilfing.model.*
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import org.springframework.beans.factory.annotation.Autowired

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
            securityLevel: SecurityLevel,
            photoGangBanger: PhotoGangBanger
    ): Article {
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
package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.model.Article
import no.fg.hilflingbackend.model.articles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
open class ArticleRepository {
  @Autowired
  open lateinit var database: Database

  fun findById(id: UUID): Article? {
    return database.articles.find { it.id eq id }
  }

  fun findAll(): List<Article> {
    return database.articles.toList()
  }

  fun create(
    article: Article
  ): Article {
    val articleFromDatabase = Article {
      this.title = article.title
      this.plainText = article.plainText
      this.securityLevel = article.securityLevel
      this.photoGangBanger = article.photoGangBanger
    }
    database.articles.add(articleFromDatabase)
    return articleFromDatabase
  }
}

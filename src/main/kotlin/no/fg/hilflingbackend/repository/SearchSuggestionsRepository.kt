package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.take
import me.liuwj.ktorm.entity.toList
import me.liuwj.ktorm.support.postgresql.ilike
import no.fg.hilflingbackend.model.motives
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
open class SearchSuggestionsRepository {

  @Autowired
  open lateinit var database: Database

  fun findBySearchTerm(searchTerm: String): List<String> =
    database
      .motives
      .filter {
        it.title ilike "%$searchTerm%"
      }.take(10)
      .toList().map { it.title }
}

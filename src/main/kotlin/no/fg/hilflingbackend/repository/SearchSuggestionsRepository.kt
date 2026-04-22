package no.fg.hilflingbackend.repository

import no.fg.hilflingbackend.model.motives
import org.ktorm.database.Database
import org.ktorm.entity.filter
import org.ktorm.entity.sortedByDescending
import org.ktorm.entity.take
import org.ktorm.entity.toList
import org.ktorm.support.postgresql.ilike
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
open class SearchSuggestionsRepository {
  @Autowired open lateinit var database: Database

  fun findBySearchTerm(searchTerm: String): List<String> =
    if (searchTerm.isBlank()) {
      // Return the 5 latest titles when search term is blank

      database.motives
        .sortedByDescending { it.dateCreated }
        .take(5)
        .toList()
        .map { it.title }
    } else {
      // Return up to 10 filtered results when search term is provided
      database.motives.filter { it.title ilike "%$searchTerm%" }.take(10).toList().map {
        it.title
      }
    }
}

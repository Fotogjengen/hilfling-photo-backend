package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.find
import no.fg.hilflingbackend.model.Search
import no.fg.hilflingbackend.model.motives
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
open class SearchRepository {

  @Autowired
  open lateinit var database: Database

  // TODO: Make return MotiveDto instead of Motive
  fun find(SearchTerm: String): Search? =
    database
      .motives
      .find {
        it.title eq SearchTerm
      }

}

package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.like
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.toList
import me.liuwj.ktorm.support.postgresql.ilike
import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
open class SearchRepository {

  @Autowired
  open lateinit var database: Database

  // TODO: Make return MotiveDto instead of Motive
  fun findBySearchterm(SearchTerm: String): List<MotiveDto> =
    database
      .motives
      .filter {
        it.title ilike "%$SearchTerm%"
      }.toList()
      .map { it.toDto() }
}

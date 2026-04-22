package no.fg.hilflingbackend.repository

import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.model.motives
import no.fg.hilflingbackend.model.toDto
import org.ktorm.database.Database
import org.ktorm.entity.filter
import org.ktorm.entity.toList
import org.ktorm.support.postgresql.ilike
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
open class SearchRepository {

  @Autowired
  open lateinit var database: Database

  fun findBySearchTerm(searchTerm: String): List<MotiveDto> =
    database
      .motives
      .filter {
        it.title ilike "%$searchTerm%"
      }.toList()
      .map { it.toDto() }
}

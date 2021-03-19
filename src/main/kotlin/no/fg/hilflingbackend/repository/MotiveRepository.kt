package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.model.Motive
import no.fg.hilflingbackend.model.motives
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
open class MotiveRepository {

  @Autowired
  open lateinit var database: Database

  fun findByTitle(title: String): Motive? =
    database
      .motives
      .find {
        it.title eq title
      }

  fun findById(id: UUID): Motive? {
    return database.motives.find { it.id eq id }
  }

  fun findAll(): List<Motive> {
    return database.motives.toList()
  }

  fun create(
    motive: Motive
  ): Int = database.motives.add(motive)
}

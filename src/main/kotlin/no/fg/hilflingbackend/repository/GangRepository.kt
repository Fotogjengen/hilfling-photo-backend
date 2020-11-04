package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.model.Gang
import no.fg.hilflingbackend.model.gangs
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
open class GangRepository {
  @Autowired
  open lateinit var database: Database

  fun findById(id: UUID): Gang? {
    return database.gangs.find { it.id eq id }
  }

  fun findAll(): List<Gang> {
    return database.gangs.toList()
  }

  fun create(gang: Gang): Int =
    database.gangs.add(gang)
}

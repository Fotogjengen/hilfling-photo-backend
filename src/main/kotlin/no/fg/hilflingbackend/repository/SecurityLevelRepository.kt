package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.model.SecurityLevel
import no.fg.hilflingbackend.model.security_levels
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
open class SecurityLevelRepository {
  @Autowired
  open lateinit var database: Database

  fun findById(id: UUID): SecurityLevel? {
    return database.security_levels.find { it.id eq id }
  }

  fun findAll(): List<SecurityLevel> {
    return database.security_levels.toList()
  }

  fun create(
    securityLevel: SecurityLevel
  ): Int {
    return database.security_levels.add(securityLevel)
  }
}

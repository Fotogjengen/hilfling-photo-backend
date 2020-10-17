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
import java.time.LocalDate

@Repository
open class SecurityLevelRepository {
  @Autowired
  open lateinit var database: Database

  fun findById(id: Int): SecurityLevel? {
    return database.security_levels.find { it.id eq id }
  }

  fun findAll(): List<SecurityLevel> {
    return database.security_levels.toList()
  }

  fun create(
    securityLevel: SecurityLevel
  ): SecurityLevel {
    val securityLevelFromDatabase = SecurityLevel {
      this.type = securityLevel.type
      this.dateCreated = LocalDate.now()
    }
    database.security_levels.add(securityLevelFromDatabase)
    return securityLevelFromDatabase
  }
}

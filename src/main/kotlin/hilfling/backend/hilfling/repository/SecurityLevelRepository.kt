package hilfling.backend.hilfling.repository

import hilfling.backend.hilfing.model.SecurityLevel
import hilfling.backend.hilfing.model.security_levels
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

class SecurityLevelRepository {
    @Autowired
    lateinit var database: Database

    fun findById(id: Long): SecurityLevel? {
        return database.security_levels.find{it.id eq  id}
    }

    fun findAll(): List<SecurityLevel> {
        return database.security_levels.toList()
    }

    fun create(type: String): SecurityLevel {
        val securityLevel = SecurityLevel{
            this.type = type
            this.dateCreated = LocalDate.now()
        }
        database.security_levels.add(securityLevel)
        return securityLevel
    }
}
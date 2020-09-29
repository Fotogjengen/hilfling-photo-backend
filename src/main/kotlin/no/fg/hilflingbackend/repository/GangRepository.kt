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

@Repository
class GangRepository {
    @Autowired
    open lateinit var database: Database

    fun findById(id: Int): Gang? {
        return database.gangs.find { it.id eq id }
    }

    fun findAll(): List<Gang> {
        return database.gangs.toList()
    }

    fun create(gang: Gang): Gang {
        val gangFromDatabase = Gang{
            this.name = gang.name
        }
        database.gangs.add(gangFromDatabase)
        return gangFromDatabase
    }
}
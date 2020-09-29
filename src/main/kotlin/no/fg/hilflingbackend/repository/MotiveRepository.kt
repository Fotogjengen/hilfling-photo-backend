package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
open class MotiveRepository {
    @Autowired
    open lateinit var database: Database

    fun findById(id: Int): Motive? {
        return database.motives.find { it.id eq id }
    }

    fun findAll(): List<Motive> {
        return database.motives.toList()
    }

    fun create(
            motive: Motive
    ): Motive {
        val motiveFromDatabase = Motive{
            this.title = motive.title
            this.category = motive.category
            this.eventOwner = motive.eventOwner
            this.album = motive.album
        }
        database.motives.add(motiveFromDatabase)
        return motiveFromDatabase
    }
}
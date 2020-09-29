package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.model.Position
import no.fg.hilflingbackend.model.positions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class PositionRepository {
    @Autowired
    open lateinit var database: Database

    fun findById(id: Int): Position? {
        return database.positions.find{it.id eq  id}
    }

    fun findAll(): List<Position> {
        return database.positions.toList()
    }

    fun create(
           position: Position
    ): Position {
        val positionFromDatabase = Position{
            this.title = position.title;
            this.email = position.email;
        }
        database.positions.add(positionFromDatabase)
        return positionFromDatabase
    }
}
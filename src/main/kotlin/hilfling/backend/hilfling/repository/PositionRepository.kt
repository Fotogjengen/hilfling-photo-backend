package hilfling.backend.hilfling.repository

import hilfling.backend.hilfling.model.Position
import hilfling.backend.hilfling.model.positions
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import org.springframework.beans.factory.annotation.Autowired

class PositionRepository {
    @Autowired
    lateinit var database: Database

    fun findById(id: Long): Position? {
        return database.positions.find{it.id eq  id}
    }

    fun findAll(): List<Position> {
        return database.positions.toList()
    }

    fun create(
           position: Position
    ): Position {
        val position = Position{
            this.title = position.title;
            this.email = position.email;
        }
        database.positions.add(position)
        return position
    }
}
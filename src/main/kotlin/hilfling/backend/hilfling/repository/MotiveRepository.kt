package hilfling.backend.hilfing.repository

import hilfling.backend.hilfing.model.*
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.from
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.where
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate

class MotiveRepository {
    @Autowired
    lateinit var database: Database

    fun findById(id: Long): Motive? {
        return database.motives.find { it.id eq id }
    }

    fun findAll(): List<Motive> {
        return database.motives.toList()
    }

    fun create(
            title: String,
            category: Category,
            eventOwner: EventOwner,
            album: Album
    ): Motive? {
        val motive = Motive{
            this.title = title
            this.category = category
            this.eventOwner = eventOwner
            this.album = album
        }
        database.motives.add(motive)
        return motive
    }
}
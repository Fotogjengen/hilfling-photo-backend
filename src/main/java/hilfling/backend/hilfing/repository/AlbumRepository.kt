package hilfling.backend.hilfing.repository

import hilfling.backend.hilfing.model.Album
import hilfling.backend.hilfing.model.albums
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate

class AlbumRepository {
    @Autowired
    lateinit var database: Database

    fun findById(id: Long): Album? {
        return database.albums.find { it.id eq id }
    }

    fun findAll(): List<Album> {
        return database.albums.toList()
    }

    fun create(title: String, isAnalog: Boolean): Album {
        val album = Album{
            this.title = title
            this.isAnalog = isAnalog
            this.timeCreated = LocalDate.now()
        }
        database.albums.add(album)
        return album
    }
}
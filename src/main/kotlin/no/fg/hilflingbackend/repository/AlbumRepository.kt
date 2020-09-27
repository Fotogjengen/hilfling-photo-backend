package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.model.Album
import no.fg.hilflingbackend.model.albums
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

class AlbumRepository {
    @Autowired
    lateinit var database: Database

    fun findById(id: Int): Album? {
        return database.albums.find { it.id eq id }
    }

    fun findAll(): List<Album> {
        return database.albums.toList()
    }

    fun create(
            album: Album
    ): Album {
        val albumFromDatabase = Album{
            this.title = album.title
            this.isAnalog = album.isAnalog
            this.dateCreated = LocalDate.now()
        }
        database.albums.add(albumFromDatabase)
        return albumFromDatabase
    }
}
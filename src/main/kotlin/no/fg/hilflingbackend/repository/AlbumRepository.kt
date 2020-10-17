package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.dto.AlbumDto
import no.fg.hilflingbackend.model.Album
import no.fg.hilflingbackend.model.albums
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
open class AlbumRepository {
  @Autowired
  open lateinit var database: Database

  fun findById(id: Int): Album? {
    return database.albums.find { it.id eq id }
  }

  fun findAll(): List<Album> {
    return database.albums.toList()
  }

  fun create(
    album: AlbumDto
  ): Int {
    return database.albums.add(
      Album {
        isAnalog = album.isAnalog
        title = album.title
      }
    )
  }
}

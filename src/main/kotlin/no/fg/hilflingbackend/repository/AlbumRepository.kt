package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.model.Album
import no.fg.hilflingbackend.model.Albums
import no.fg.hilflingbackend.model.albums
import org.springframework.stereotype.Repository

@Repository
open class AlbumRepository(database: Database) : BaseRepository<Album>(table = Albums, database) {
  override fun convertToClass(qrs: QueryRowSet): Album {
    return Album{
      id = qrs[Albums.id]!!
      dateCreated = qrs[Albums.dateCreated]!!
      title = qrs[Albums.title]!!
      isAnalog = qrs[Albums.isAnalog]!!
    }
  }

  override fun create(entity: Album): Int {
    return database.albums.add(entity)
  }

  override fun patch(entity: Album): Int {
    return database.albums.update(entity)
  }
}

package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.dto.AlbumDto
import no.fg.hilflingbackend.dto.AlbumId
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.Album
import no.fg.hilflingbackend.model.Albums
import no.fg.hilflingbackend.model.albums
import no.fg.hilflingbackend.model.toDto
import org.springframework.stereotype.Repository

@Repository
open class AlbumRepository(database: Database) : BaseRepository<Album, AlbumDto>(table = Albums, database) {
  override fun convertToClass(qrs: QueryRowSet): AlbumDto = AlbumDto(
      albumId = AlbumId(qrs[Albums.id]!!),
      title = qrs[Albums.title]!!,
      isAnalog = qrs[Albums.isAnalog]!!
  )

  override fun create(dto: AlbumDto): Int {
    return database.albums.add(dto.toEntity())
  }

  override fun patch(dto: AlbumDto): Int {
    return database.albums.update(dto.toEntity())
  }
  fun test2() {

  }
}

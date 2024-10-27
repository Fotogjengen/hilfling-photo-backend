package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.dto.AlbumDto
import no.fg.hilflingbackend.dto.AlbumId
import no.fg.hilflingbackend.dto.AlbumPatchRequestDto
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.Album
import no.fg.hilflingbackend.model.Albums
import no.fg.hilflingbackend.model.albums
import org.springframework.stereotype.Repository
import jakarta.persistence.EntityNotFoundException

@Repository
open class AlbumRepository(database: Database) : BaseRepository<Album, AlbumDto, AlbumPatchRequestDto>(table = Albums, database = database) {
  override fun convertToClass(qrs: QueryRowSet): AlbumDto = AlbumDto(
    albumId = AlbumId(qrs[Albums.id]!!),
    title = qrs[Albums.title]!!,
    isAnalog = qrs[Albums.isAnalog]!!
  )

  override fun create(dto: AlbumDto): Int {
    return database.albums.add(dto.toEntity())
  }

  override fun patch(dto: AlbumPatchRequestDto): AlbumDto {
    val fromDb = findById(dto.albumId.id)
      ?: throw EntityNotFoundException("Could not find SecurityLevel")
    val newDto = AlbumDto(
      albumId = fromDb.albumId,
      title = dto.title ?: fromDb.title,
      isAnalog = dto.isAnalog ?: fromDb.isAnalog
    )
    val updated = database.albums.update(newDto.toEntity())

    return if (updated == 1) newDto else fromDb
  }
}

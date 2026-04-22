package no.fg.hilflingbackend.repository

import jakarta.persistence.EntityNotFoundException
import no.fg.hilflingbackend.dto.AlbumDto
import no.fg.hilflingbackend.dto.AlbumId
import no.fg.hilflingbackend.dto.AlbumPatchRequestDto
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.Album
import no.fg.hilflingbackend.model.Albums
import no.fg.hilflingbackend.model.albums
import org.ktorm.database.Database
import org.ktorm.dsl.QueryRowSet
import org.ktorm.dsl.eq
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.ktorm.entity.add
import org.ktorm.entity.any
import org.ktorm.entity.update
import org.springframework.stereotype.Repository

@Repository
open class AlbumRepository(database: Database) : BaseRepository<Album, AlbumDto, AlbumPatchRequestDto>(table = Albums, database = database) {
  override fun convertToClass(qrs: QueryRowSet): AlbumDto = AlbumDto(
    albumId = AlbumId(qrs[Albums.id]!!),
    title = qrs[Albums.title]!!,
    isAnalog = qrs[Albums.isAnalog]!!
  )

  override fun create(dto: AlbumDto): Int {
    val exists = database.albums.any { it.id eq dto.albumId.id }
    if (exists) return 0
    database.albums.add(dto.toEntity())
    return 1
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

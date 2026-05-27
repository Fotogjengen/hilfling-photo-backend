package no.fg.hilflingbackend.repository

import jakarta.persistence.EntityNotFoundException
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.desc
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.from
import me.liuwj.ktorm.dsl.isNull
import me.liuwj.ktorm.dsl.limit
import me.liuwj.ktorm.dsl.map
import me.liuwj.ktorm.dsl.orderBy
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.where
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.any
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.dto.AlbumDto
import no.fg.hilflingbackend.dto.AlbumId
import no.fg.hilflingbackend.dto.AlbumPatchRequestDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.Album
import no.fg.hilflingbackend.model.Albums
import no.fg.hilflingbackend.model.albums
import no.fg.hilflingbackend.model.toDto
import org.springframework.stereotype.Repository

@Repository
open class AlbumRepository(
  database: Database,
) : BaseRepository<Album, AlbumDto, AlbumPatchRequestDto>(table = Albums, database = database) {
  override fun convertToClass(qrs: QueryRowSet): AlbumDto =
    AlbumDto(
      albumId = AlbumId(qrs[Albums.id]!!),
      name = qrs[Albums.name]!!,
      description = qrs[Albums.description],
      analog = qrs[Albums.analog]!!,
    )

  override fun create(dto: AlbumDto): Int {
    val exists = database.albums.any { it.id eq dto.albumId.id }
    if (exists) return 0
    database.albums.add(dto.toEntity())
    return 1
  }

  fun findByName(name: String): AlbumDto? = database.albums.find { it.name eq name }?.toDto()

  fun findAllAnalog(page: Int, pageSize: Int): Page<AlbumDto> {
    val offset = page * pageSize
    val resultSet =
      database
        .from(Albums)
        .select()
        .where { (Albums.analog eq true) and (Albums.dateDeleted.isNull()) }
        .orderBy(Albums.dateCreated.desc())
        .limit(offset, pageSize)
    return Page(
      page = page,
      pageSize = pageSize,
      totalRecords = resultSet.totalRecords,
      currentList = resultSet.map { row -> convertToClass(row) },
    )
  }

  fun findAllDigital(page: Int, pageSize: Int): Page<AlbumDto> {
    val offset = page * pageSize
    val resultSet =
      database
        .from(Albums)
        .select()
        .where { (Albums.analog eq false) and (Albums.dateDeleted.isNull()) }
        .orderBy(Albums.dateCreated.desc())
        .limit(offset, pageSize)
    return Page(
      page = page,
      pageSize = pageSize,
      totalRecords = resultSet.totalRecords,
      currentList = resultSet.map { row -> convertToClass(row) },
    )
  }

  override fun patch(dto: AlbumPatchRequestDto): AlbumDto {
    val fromDb =
      findById(dto.albumId.id)
        ?: throw EntityNotFoundException("Could not find album with id ${dto.albumId.id}")
    val newDto =
      AlbumDto(
        albumId = fromDb.albumId,
        name = dto.name,
        description = dto.description ?: fromDb.description,
        analog = dto.analog ?: fromDb.analog,
      )
    val updated = database.albums.update(newDto.toEntity())

    return if (updated == 1) newDto else fromDb
  }
}

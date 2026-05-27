package no.fg.hilflingbackend.repository

import jakarta.persistence.EntityNotFoundException
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.isNull
import me.liuwj.ktorm.dsl.update
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.drop
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.sortedByDescending
import me.liuwj.ktorm.entity.take
import me.liuwj.ktorm.entity.toList
import me.liuwj.ktorm.entity.update
import me.liuwj.ktorm.support.postgresql.ilike
import no.fg.hilflingbackend.dto.MotiveCreateRequestDto
import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.dto.MotiveId
import no.fg.hilflingbackend.dto.MotivePatchRequestDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.Motive
import no.fg.hilflingbackend.model.Motives
import no.fg.hilflingbackend.model.motives
import no.fg.hilflingbackend.model.toDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.UUID

@Repository
open class MotiveRepository {
  @Autowired
  open lateinit var database: Database

  // TODO: Make return MotiveDto instead of Motive
  fun findByTitle(title: String): Motive? =
    database
      .motives
      .find {
        (it.title eq title) and it.dateDeleted.isNull()
      }

  fun findById(id: UUID): MotiveDto? =
    database.motives.find { (it.id eq id) and it.dateDeleted.isNull() }?.toDto()

  fun findAll(
    page: Int,
    pageSize: Int,
  ): Page<MotiveDto> {
    val sequence = database.motives.filter { it.dateDeleted.isNull() }
    val motiveDtos =
      sequence
        .sortedByDescending { it.dateCreated }
        .drop(page * pageSize)
        .take(pageSize)
        .toList()
        .map { it.toDto() }

    return Page(
      page = page,
      pageSize = pageSize,
      totalRecords = sequence.totalRecords,
      currentList = motiveDtos,
    )
  }

  fun search(
    searchTerm: String,
    page: Int,
    pageSize: Int,
  ): Page<MotiveDto> {
    val sequence = database.motives.filter { (it.title ilike "%$searchTerm%") and it.dateDeleted.isNull() }
    val motiveDtos =
      sequence
        .sortedByDescending { it.dateCreated }
        .drop(page * pageSize)
        .take(pageSize)
        .toList()
        .map { it.toDto() }

    return Page(
      page = page,
      pageSize = pageSize,
      totalRecords = sequence.totalRecords,
      currentList = motiveDtos,
    )
  }

  fun create(
    motive: MotiveCreateRequestDto,
  ): MotiveDto {
    val dto = MotiveDto(
      motiveId = MotiveId(),
      title = motive.title,
      date = motive.date,
      categoryDto = motive.categoryDto,
      eventOwnerDto = motive.eventOwnerDto,
      placeDto = motive.placeDto,
      securityLevel = motive.securityLevel,
      albumDto = motive.albumDto,
      analogAlbumDto = motive.analogAlbumDto,
      dateCreated = LocalDate.now(),
    )
    database.motives.add(dto.toEntity())
    return dto
  }

  fun delete(id: UUID): Int =
    database.update(Motives) {
      set(it.dateDeleted, LocalDate.now())
      where { it.id eq id }
    }

  fun patch(dto: MotivePatchRequestDto): MotiveDto {
    val fromDb =
      findById(dto.motiveId.id)
        ?: throw EntityNotFoundException("Could not find Motive")
    val newDto =
      MotiveDto(
        motiveId = fromDb.motiveId,
        title = dto.title ?: fromDb.title,
        date = dto.date ?: fromDb.date,
        categoryDto = dto.categoryDto ?: fromDb.categoryDto,
        eventOwnerDto = dto.eventOwnerDto ?: fromDb.eventOwnerDto,
        placeDto = dto.placeDto ?: fromDb.placeDto,
        securityLevel = dto.securityLevel ?: fromDb.securityLevel,
        albumDto = dto.albumDto ?: fromDb.albumDto,
        analogAlbumDto = dto.analogAlbumDto ?: fromDb.analogAlbumDto,
        dateCreated = fromDb.dateCreated,
      )
    val updated = database.motives.update(newDto.toEntity())

    return if (updated == 1) newDto else fromDb
  }
}

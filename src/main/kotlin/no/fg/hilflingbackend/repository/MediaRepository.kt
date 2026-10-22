package no.fg.hilflingbackend.repository

import jakarta.persistence.EntityNotFoundException
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.isNull
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.any
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.dto.MediaDto
import no.fg.hilflingbackend.dto.MediaId
import no.fg.hilflingbackend.dto.MediaPatchRequestDto
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.Media
import no.fg.hilflingbackend.model.Medias
import no.fg.hilflingbackend.model.medias
import no.fg.hilflingbackend.model.photos
import no.fg.hilflingbackend.model.toDto
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
open class MediaRepository(
  database: Database,
) : BaseRepository<Media, MediaDto, MediaPatchRequestDto>(table = Medias, database = database) {
  override fun convertToClass(qrs: QueryRowSet): MediaDto =
    MediaDto(
      mediaId = MediaId(qrs[Medias.id]!!),
      medium = qrs[Medias.medium]!!,
      analog = qrs[Medias.analog]!!,
    )

  override fun create(dto: MediaDto): Int = database.medias.add(dto.toEntity())

  fun findOrNull(id: UUID): MediaDto? = database.medias.find { (it.id eq id) and it.dateDeleted.isNull() }?.toDto()

  fun findByMediumOrNull(medium: String): MediaDto? = database.medias.find { (it.medium eq medium) and it.dateDeleted.isNull() }?.toDto()

  fun isReferenced(id: UUID): Boolean = database.photos.any { it.mediaId eq id }

  override fun patch(dto: MediaPatchRequestDto): MediaDto {
    val fromDb =
      findOrNull(dto.mediaId.id)
        ?: throw EntityNotFoundException("Could not find Media")
    val newDto =
      MediaDto(
        mediaId = fromDb.mediaId,
        medium = dto.medium ?: fromDb.medium,
        analog = dto.analog ?: fromDb.analog,
      )
    val updated = database.medias.update(newDto.toEntity())

    return if (updated == 1) newDto else fromDb
  }
}

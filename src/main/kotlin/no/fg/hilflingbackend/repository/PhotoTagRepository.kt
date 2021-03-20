package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.dto.PhotoTagDto
import no.fg.hilflingbackend.dto.PhotoTagId
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.PhotoTag
import no.fg.hilflingbackend.model.PhotoTags
import no.fg.hilflingbackend.model.photo_tags
import no.fg.hilflingbackend.model.toDto
import org.springframework.stereotype.Repository

@Repository
open class PhotoTagRepository(database: Database) : BaseRepository<PhotoTag, PhotoTagDto>(table = PhotoTags, database = database) {
  override fun convertToClass(qrs: QueryRowSet): PhotoTagDto = PhotoTagDto(
    photoTagId = PhotoTagId(qrs[PhotoTags.id]!!),
    name = qrs[PhotoTags.name]!!
  )

  override fun create(dto: PhotoTagDto): Int {
    return database.photo_tags.add(dto.toEntity())
  }

  override fun patch(dto: PhotoTagDto): Int {
    return database.photo_tags.update(dto.toEntity())
  }

  fun findByName(photoTagName: String) = database
    .photo_tags
    .find {
      it.name eq photoTagName
    }
    ?.toDto()
}

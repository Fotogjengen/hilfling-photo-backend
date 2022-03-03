package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.delete
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.removeIf
import me.liuwj.ktorm.entity.toList
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.dto.PhotoId
import no.fg.hilflingbackend.dto.PhotoTagDto
import no.fg.hilflingbackend.dto.PhotoTagId
import no.fg.hilflingbackend.dto.PhotoTagPatchRequestDto
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.PhotoTag
import no.fg.hilflingbackend.model.PhotoTagReference
import no.fg.hilflingbackend.model.PhotoTagReferences
import no.fg.hilflingbackend.model.PhotoTags
import no.fg.hilflingbackend.model.photo_tag_references
import no.fg.hilflingbackend.model.photo_tags
import no.fg.hilflingbackend.model.toDto
import org.springframework.stereotype.Repository
import java.util.UUID
import javax.persistence.EntityNotFoundException

@Repository
open class PhotoTagRepository(database: Database) : BaseRepository<PhotoTag, PhotoTagDto, PhotoTagPatchRequestDto>(table = PhotoTags, database = database) {
  override fun convertToClass(qrs: QueryRowSet): PhotoTagDto = PhotoTagDto(
    photoTagId = PhotoTagId(qrs[PhotoTags.id]!!),
    name = qrs[PhotoTags.name]!!
  )

  override fun create(dto: PhotoTagDto): Int {
    return database.photo_tags.add(dto.toEntity())
  }

  override fun patch(dto: PhotoTagPatchRequestDto): PhotoTagDto {
    val fromDb = findById(dto.photoTagId.id)
      ?: throw EntityNotFoundException("Could not find PhotoTag")
    val newDto = PhotoTagDto(
      photoTagId = fromDb.photoTagId,
      name = dto.name ?: fromDb.name
    )
    val updated = database.photo_tags.update(newDto.toEntity())

    return if (updated == 1) newDto else fromDb
  }

  fun findByName(photoTagName: String) = database
    .photo_tags
    .find {
      it.name eq photoTagName
    }
    ?.toDto()

  fun deletePhotoTagReference(photoTagId: PhotoTagId, photoId: PhotoId): Int {
    val reference = database.photo_tag_references.find {
      it.photoId eq photoId.id
      it.photoTagId eq photoTagId.id
    } ?: return 0
    return reference.delete()
    /*return database.photo_tag_references.removeIf {
      it.photoTagId eq photoTagId.id
      it.photoId eq photoId.id
    }*/
    /*return database.delete(PhotoTagReferences) {
      it.id eq tag.id
      *//*it.photoTagId eq photoTagId*//*
    }*/
  }
}

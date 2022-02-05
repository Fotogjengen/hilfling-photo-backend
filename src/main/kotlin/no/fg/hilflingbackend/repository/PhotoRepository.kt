package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.take
import me.liuwj.ktorm.entity.toList
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.dto.PhotoTagDto
import no.fg.hilflingbackend.dto.PhotoTagId
/* ktlint-disable no-wildcard-imports */
import no.fg.hilflingbackend.model.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.util.UUID

/* TODO Remove this?
fun Database.getPhotoAndPhotoTags(filter: () -> ColumnDeclaring<Boolean>){
  return this
    .from(Photos)
    .crossJoin(PhotoTags)
    .select(
      Photos.id,
      PhotoTags.name
    )
}

 */
@Repository
open class PhotoRepository(
  val database: Database
) {
  val logger = LoggerFactory.getLogger(this::class.java)

  private fun findCorrespondingPhotoTagDtos(photo: Photo): List<PhotoTagDto> {
    return database.from(PhotoTags)
      .crossJoin(PhotoTagReferences)
      .select(
        PhotoTags.name,
        PhotoTags.id,
        PhotoTagReferences.photoId,
        PhotoTagReferences.photoTagId
      )
      .where { PhotoTagReferences.photoId eq photo.id }
      .map { row -> PhotoTagDto(
        photoTagId = PhotoTagId(row[PhotoTags.id]!!),
        name = row[PhotoTags.name]!!
      ) }
  }

  fun findById(id: UUID): PhotoDto? {
    return database.photos.find { it.id eq id }
      ?.let { photo ->
        return photo.toDto(
          findCorrespondingPhotoTagDtos(photo)
        )
      }
  }

  fun findByMotiveId(id: UUID): List<PhotoDto>? {
    return database.photos.filter {
        it.motiveId eq id
      }.toList()
      .map { it.toDto(findCorrespondingPhotoTagDtos(it)) }
  }

  fun findAnalogPhotoById(id: UUID): AnalogPhoto? {
    return database.analog_photos.find { it.id eq id }
  }

  fun findAll(): List<PhotoDto> {
    return database.photos.toList()
      .map { it.toDto(
        findCorrespondingPhotoTagDtos(it)
      ) }
  }

  fun findAllAnalogPhotos(): List<PhotoDto> {
    return database
      .photos
      .filter {
        val motive = it.motiveId.referenceTable as Motives
        val album = motive.albumId.referenceTable as Albums
        album.isAnalog eq true
      }
      .toList()
      .map { it.toDto(findCorrespondingPhotoTagDtos(it)) }
  }

  fun findAllDigitalPhotos(): List<PhotoDto> {
    return database
      .photos
      .filter {
        val motive = it.motiveId.referenceTable as Motives
        val album = motive.albumId.referenceTable as Albums
        album.isAnalog eq false
      }.toList()
      .map { it.toDto(findCorrespondingPhotoTagDtos(it)) }
  }

  fun findCarouselPhotos(): List<PhotoDto> {
    return database
      .photos
      .filter { it.isGoodPicture eq true }
      .take(6).toList()
      .map { it.toDto(findCorrespondingPhotoTagDtos(it)) }
  }

  fun findBySecurityLevel(securityLevel: SecurityLevel): List<PhotoDto> {
    return database
      .photos
      .filter {
        val securityLevelFromDatabase = it.securityLevelId.referenceTable as SecurityLevels
        securityLevelFromDatabase.id eq securityLevel.id
      }.toList()
      .map { it.toDto(findCorrespondingPhotoTagDtos(it)) }
  }

  fun createPhoto(
    photoDto: PhotoDto
  ): Int {
    logger.info("Storing photo ${photoDto.photoId.id} to database")

    val numOfSavedPhotos = database.photos.add(photoDto.toEntity())
    // TODO: Rewrite to batchInsert for perfomance gains
    // TODO: Add photoTags as well
    /*
    photoDto.photoTags.forEach { photoTagDto: PhotoTagDto ->
      logger.info("Adding photoTag ${photoTagDto.name} to ${photoDto.photoId.id}")
      database.insert(PhotoTagReferences) {
        set(it.id, UUID.randomUUID())
        set(it.photoId, photoDto.photoId.id)
        set(it.photoTagId, photoTagDto.photoTagId.id)
      }
    }
     */
    logger.info("Storing photo tags to database")
    val photoTagDtoList = photoDto.photoTags
    database.batchInsert(PhotoTagReferences){
      photoTagDtoList.map { photoTagDto -> item {
        set(it.id, UUID.randomUUID())
        set(it.photoTagId, photoTagDto.photoTagId.id)
        set(it.photoId, photoDto.photoId.id)
      } }
    }

    return numOfSavedPhotos
  }

  fun createAnalogPhoto(
    analogPhoto: AnalogPhoto
  ): AnalogPhoto {
    TODO("Implement")
    /*
    val photoFromDatabase = createPhoto(
      analogPhoto.photo
    )
    val analogPhotoFromDatabase = AnalogPhoto {
      this.imageNumber = analogPhoto.imageNumber
      this.pageNumber = analogPhoto.pageNumber
      this.photo = analogPhoto.photo
    }
    database.analog_photos.add(analogPhotoFromDatabase)
    return analogPhotoFromDatabase

     */
  }

  fun patchAnalogPhoto(
    analogPhoto: AnalogPhoto
  ): AnalogPhoto? {
    database
      .analog_photos
      .update(analogPhoto)
    return findAnalogPhotoById(analogPhoto.id)
  }
}

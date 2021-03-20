package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.batchInsert
import me.liuwj.ktorm.dsl.crossJoin
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.from
import me.liuwj.ktorm.dsl.insert
import me.liuwj.ktorm.dsl.insertAndGenerateKey
import me.liuwj.ktorm.dsl.map
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.where
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.take
import me.liuwj.ktorm.entity.toList
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.dto.PhotoTagDto
import no.fg.hilflingbackend.dto.PhotoTagId
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.Albums
import no.fg.hilflingbackend.model.AnalogPhoto
import no.fg.hilflingbackend.model.Motives
import no.fg.hilflingbackend.model.PhotoTagReference
import no.fg.hilflingbackend.model.PhotoTagReferences
import no.fg.hilflingbackend.model.PhotoTags
import no.fg.hilflingbackend.model.SecurityLevel
import no.fg.hilflingbackend.model.SecurityLevels
import no.fg.hilflingbackend.model.analog_photos
import no.fg.hilflingbackend.model.photo_tag_references
import no.fg.hilflingbackend.model.photo_tags
import no.fg.hilflingbackend.model.photos
import no.fg.hilflingbackend.model.toDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.UUID

/*
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
open class PhotoRepository {
  @Autowired
  open lateinit var database: Database

  val logger = LoggerFactory.getLogger(this::class.java)

  fun findById(id: UUID): PhotoDto? {
    return database.photos.find { it.id eq id }
      ?.let{ photo ->
        val tags = database
          .from(PhotoTags)
          .crossJoin(PhotoTagReferences)
          .select(
            PhotoTags.id,
            PhotoTags.name,
            PhotoTagReferences.photoId,
            PhotoTagReferences.photoTagId
          )
          .where { PhotoTagReferences.photoId eq photo.id }
          .map { row ->
            PhotoTagDto(
              // TODO: Try to avoid !! null safety override
              photoTagId = PhotoTagId(row[PhotoTags.id]!!),
              name = row[PhotoTags.name]!!
            )
          }
        return photo.toDto(tags)
      }
  }
  fun findAnalogPhotoById(id: UUID): AnalogPhoto? {
    return database.analog_photos.find { it.id eq id }
  }

  fun findAll(): List<PhotoDto> {
    return database
      .photos
      .toList()
      .map { it.toDto() }
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
      .map { it.toDto() }
  }

  fun findAllDigitalPhotos(): List<PhotoDto> {
    return database
      .photos
      .filter {
        val motive = it.motiveId.referenceTable as Motives
        val album = motive.albumId.referenceTable as Albums
        album.isAnalog eq false
      }.toList()
      .map { it.toDto() }
  }

  fun findCarouselPhotos(): List<PhotoDto> {
    return database
      .photos
      .filter { it.isGoodPicture eq true }
      .take(6).toList()
      .map { it.toDto() }
  }

  fun findBySecurityLevel(securityLevel: SecurityLevel): List<PhotoDto> {
    return database
      .photos
      .filter {
        val securityLevelFromDatabase = it.securityLevelId.referenceTable as SecurityLevels
        securityLevelFromDatabase.id eq securityLevel.id
      }.toList()
      .map { it.toDto() }
  }

  fun createPhoto(
    photoDto: PhotoDto
  ): Int {
    logger.info("Storing photo ${photoDto.photoId.id} to database")

    val numOfSavedPhotos = database.photos.add(photoDto.toEntity())
    // TODO: Rewrite to batchInsert for perfomance gains
    println(photoDto)
    photoDto.photoTags.forEach { photoTagDto: PhotoTagDto ->
      println("Adding photoTag $photoTagDto.name")
      database.insert(PhotoTags) {
        set(it.id, photoTagDto.photoTagId.id)
        set(it.name, photoTagDto.name)
      }
      database.insert(PhotoTagReferences) {
        set(it.id, UUID.randomUUID())
        set(it.photoId, photoDto.photoId.id)
        set(it.photoTagId, photoTagDto.photoTagId.id)
      }
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

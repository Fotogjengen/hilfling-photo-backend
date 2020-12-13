package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.take
import me.liuwj.ktorm.entity.toList
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.model.Albums
import no.fg.hilflingbackend.model.AnalogPhoto
import no.fg.hilflingbackend.model.Motives
import no.fg.hilflingbackend.model.Photo
import no.fg.hilflingbackend.model.SecurityLevel
import no.fg.hilflingbackend.model.SecurityLevels
import no.fg.hilflingbackend.model.analog_photos
import no.fg.hilflingbackend.model.photos
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
open class PhotoRepository {
  @Autowired
  open lateinit var database: Database

  fun findById(id: UUID): Photo? {
    return database.photos.find { it.id eq id }
  }

  fun findAnalogPhotoById(id: UUID): AnalogPhoto? {
    return database.analog_photos.find { it.id eq id }
  }

  fun findAll(): List<Photo> {
    return database.photos.toList()
  }

  fun findAllAnalogPhotos(): List<Photo> {
    return database.photos.filter {
      val motive = it.motiveId.referenceTable as Motives
      val album = motive.albumId.referenceTable as Albums
      album.isAnalog eq true
    }.toList()
  }

  fun findAllDigitalPhotos(): List<Photo> {
    return database.photos.filter {
      val motive = it.motiveId.referenceTable as Motives
      val album = motive.albumId.referenceTable as Albums
      album.isAnalog eq false
    }.toList()
  }

  fun findCarouselPhotos(): List<Photo> {
    return database.photos
      .filter { it.isGoodPicture eq true }
      .take(6).toList()
  }

  fun findBySecurityLevel(securityLevel: SecurityLevel): List<Photo> {
    return database.photos.filter {
      val securityLevelFromDatabase = it.securityLevelId.referenceTable as SecurityLevels
      securityLevelFromDatabase.id eq securityLevel.id
    }.toList()
  }

  // TODO: Refactor to use DTO
  fun createPhoto(
    photo: Photo
  ): Int = database.photos.add(photo)

  fun createAnalogPhoto(
    analogPhoto: AnalogPhoto
  ): AnalogPhoto {
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
  }

  fun patchAnalogPhoto(
    analogPhoto: AnalogPhoto
  ): AnalogPhoto? {
    database.analog_photos.update(analogPhoto)
    return findAnalogPhotoById(analogPhoto.id)
  }
}

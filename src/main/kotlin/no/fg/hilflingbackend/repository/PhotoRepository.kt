package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.*
import no.fg.hilflingbackend.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

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

  fun createPhoto(
    photo: Photo
  ): Photo {
    val photoFromDatabase = Photo {
      this.isGoodPicture = photo.isGoodPicture
      this.smallUrl = photo.smallUrl
      this.mediumUrl = photo.mediumUrl
      this.largeUrl = photo.largeUrl
      this.motive = database.motives.find { it.id eq photo.motive.id }
        ?: throw IllegalAccessError("Motive does not exist.")
      this.place = database.places.find { it.id eq photo.place.id }
        ?: throw IllegalAccessError("Place does not exist.")
      this.securityLevel = database.security_levels.find { it.id eq photo.securityLevel.id }
        ?: throw IllegalAccessError("Security level does not exist.")
      this.gang = database.gangs.find { it.id eq photo.gang.id }
        ?: throw IllegalAccessError("Gang does not exist.")
      this.photoGangBanger = database.photo_gang_bangers.find { it.id eq photo.photoGangBanger.id }
        ?: throw IllegalAccessError("Photo gang banger does not exist.")
    }
    database.photos.add(photoFromDatabase)
    return photoFromDatabase
  }

  fun createAnalogPhoto(
    analogPhoto: AnalogPhoto
  ): AnalogPhoto {
    val photoFromDatabase = createPhoto(
      analogPhoto.photo
    )
    val analogPhotoFromDatabase = AnalogPhoto {
      this.imageNumber = analogPhoto.imageNumber
      this.pageNumber = analogPhoto.pageNumber
      this.photo = photoFromDatabase
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

package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
open class PhotoRepository {
    @Autowired
    open lateinit var database: Database

    fun findById(id: Int): Photo? {
        return database.photos.find { it.id eq id }
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

    fun findBySecurityLevel(securityLevel: SecurityLevel): List<Photo> {
        return database.photos.filter {
            val securityLevelFromDatabase = it.securityLevelId.referenceTable as SecurityLevels
            securityLevelFromDatabase.id eq securityLevel.id
        }.toList()
    }

    fun createPhoto(
            photo: Photo
    ): Photo? {
        val photoFromDatabase = Photo{
            this.isGoodPicture = photo.isGoodPicture
            this.smallUrl = photo.smallUrl
            this.mediumUrl = photo.mediumUrl
            this.largeUrl = photo.largeUrl
            this.motive = database.motives.find { it.id eq photo.motive.id } ?: return null
            this.place = database.places.find{it.id eq photo.place.id} ?: return null
            this.securityLevel = database.security_levels.find{it.id eq photo.securityLevel.id} ?: return null
            this.gang = database.gangs.find{it.id eq photo.gang.id} ?: return null
            this.photoGangBanger = database.photo_gang_bangers.find{it.id eq photo.photoGangBanger.id} ?: return null
        }
        database.photos.add(photoFromDatabase)
        return photoFromDatabase
    }

    fun createAnalogPhoto(
            analogPhoto: AnalogPhoto
    ): AnalogPhoto? {
        val photoFromDatabase = createPhoto(
                analogPhoto.photo
        ) ?: return null
        val analogPhotoFromDatabase = AnalogPhoto{
            this.imageNumber = analogPhoto.imageNumber
            this.pageNumber = analogPhoto.pageNumber
            this.photo = photoFromDatabase
        }
        database.analog_photos.add(analogPhotoFromDatabase)
        return analogPhotoFromDatabase
    }
}
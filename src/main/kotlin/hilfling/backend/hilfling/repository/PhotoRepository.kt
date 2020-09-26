package hilfling.backend.hilfling.repository

import hilfling.backend.hilfling.model.*
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import org.springframework.beans.factory.annotation.Autowired

class PhotoRepository {
    @Autowired
    lateinit var database: Database

    fun findById(id: Long): Photo? {
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
    ): Photo {

        val createdPhoto = Photo{
            this.isGoodPicture = photo.isGoodPicture
            this.smallUrl = photo.smallUrl
            this.mediumUrl = photo.mediumUrl
            this.largeUrl = photo.largeUrl
            this.motive = photo.motive
            this.place = photo.place
            this.securityLevel = photo.securityLevel
            this.gang = photo.gang
            this.photoGangBanger = photo.photoGangBanger
        }
        database.photos.add(createdPhoto)
        return createdPhoto
    }

    fun createAnalogPhoto(
            analogPhoto: AnalogPhoto
    ): AnalogPhoto {
        val createdPhoto = createPhoto(
                analogPhoto.photo
        )
        val createdAnalogPhoto = AnalogPhoto{
            this.imageNumber = analogPhoto.imageNumber
            this.pageNumber = analogPhoto.pageNumber
            this.photo = createdPhoto
        }
        database.analog_photos.add(createdAnalogPhoto)
        return createdAnalogPhoto
    }
}
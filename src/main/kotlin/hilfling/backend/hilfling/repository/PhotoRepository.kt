package hilfling.backend.hilfing.repository

import hilfling.backend.hilfing.model.*
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
            val album = motive.album.referenceTable as Albums
            album.isAnalog eq true
        }.toList()
    }

    fun findAllDigitalPhotos(): List<Photo> {
        return database.photos.filter {
            val motive = it.motiveId.referenceTable as Motives
            val album = motive.album.referenceTable as Albums
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
            isGoodPicture: Boolean,
            smallUrl: String,
            mediumUrl: String,
            largeUrl: String,
            motiveId: Long,
            placeId: Long,
            securityLevelId: Long,
            gangId: Long,
            photoGangBangerId: Long
    ): Photo? {
        val motive = database.motives.find { it.id eq motiveId } ?: return null
        val place = database.places.find { it.id eq placeId } ?: return null
        val securityLevel = database.security_levels.find { it.id eq securityLevelId } ?: return null
        val gang = database.gangs.find { it.id eq gangId } ?: return null
        val photoGangBanger = database.photo_gang_bangers.find { it.id eq photoGangBangerId } ?: return null

        val photo = Photo{
            this.isGoodPicture = isGoodPicture
            this.smallUrl = smallUrl
            this.mediumUrl = mediumUrl
            this.largeUrl = largeUrl
            this.motive = motive
            this.place = place
            this.securityLevel = securityLevel
            this.gang = gang
            this.photoGangBanger = photoGangBanger
        }
        database.photos.add(photo)
        return photo
    }

    fun createAnalogPhoto(
            imageNumber: Int,
            pageNumber: Int,
            isGoodPicture: Boolean,
            smallUrl: String,
            mediumUrl: String,
            largeUrl: String,
            motiveId: Long,
            placeId: Long,
            securityLevelId: Long,
            gangId: Long,
            photoGangBangerId: Long
    ): AnalogPhoto? {
        val photo = createPhoto(
                isGoodPicture,
                smallUrl,
                mediumUrl,
                largeUrl,
                motiveId,
                placeId,
                securityLevelId,
                gangId,
                photoGangBangerId
        ) ?: return null
        val analogPhoto = AnalogPhoto{
            this.imageNumber = imageNumber
            this.pageNumber = pageNumber
            this.photo = photo
        }
        database.analog_photos.add(analogPhoto)
        return analogPhoto
    }
}
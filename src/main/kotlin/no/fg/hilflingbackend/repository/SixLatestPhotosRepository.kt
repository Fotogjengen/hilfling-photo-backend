package no.fg.hilflingbackend.repository


import me.liuwj.ktorm.database.Database
import org.springframework.beans.factory.annotation.Autowired
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.model.Photo
import no.fg.hilflingbackend.model.photos
import org.springframework.stereotype.Repository

@Repository
open class SixLatestPhotosRepository {
    @Autowired
    open lateinit var database: Database

    fun findSixLatestPhotos(): Photos {
        return database.photos.toList().subList(1,6)
    }


    fun create(
            photo: Photo
    ): Photo {
        val photoFromDatabase = Photo {
            this.isGoodPicture = photo.isGoodPictire

            this.smallUrl = photo.smallUrl
            this.mediumUrl = photo.mediumUrl
            this.largeUrl = photo.largeUrl

            this.motive = photo.motive
            this.place = photo.place
            this.securityLevel = photo.securityLevel
            this.gang = photo.gang
            this.photoGangBanger = photo.photoGangBanger

        }
        database.users.add(userFromDatabase)
        return userFromDatabase
    }
}

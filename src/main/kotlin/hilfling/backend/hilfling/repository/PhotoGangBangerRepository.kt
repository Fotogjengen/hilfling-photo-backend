package hilfling.backend.hilfling.repository

import hilfling.backend.hilfling.model.PhotoGangBanger
import hilfling.backend.hilfling.model.User
import hilfling.backend.hilfling.model.photo_gang_bangers
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import org.springframework.beans.factory.annotation.Autowired

class PhotoGangBangerRepository {
    // TODO: Join with PhotoGangBangerPositions
    @Autowired
    lateinit var database: Database

    fun findById(id: Int): PhotoGangBanger? {
        return database.photo_gang_bangers.find{it.id eq  id}
    }

    fun findAll(): List<PhotoGangBanger> {
        return database.photo_gang_bangers.toList()
    }

    fun findAllActives(): List<PhotoGangBanger> {
        return database.photo_gang_bangers.filter {
            it.isActive eq true
            it.isPang eq false
        }.toList()
    }

    fun findAllActivePangs(): List<PhotoGangBanger> {
        return database.photo_gang_bangers.filter {
            it.isActive eq true
            it.isPang eq true
        }.toList()
    }

    fun findAllInActivePangs(): List<PhotoGangBanger> {
        return database.photo_gang_bangers.filter {
            it.isActive eq false
            it.isPang eq true
        }.toList()
    }

    fun create(
            photoGangBanger: PhotoGangBanger
    ): PhotoGangBanger {
        val photoGangBanger = PhotoGangBanger{
            this.relationshipStatus = photoGangBanger.relationshipStatus;
            this.semesterStart = photoGangBanger.semesterStart;
            this.isActive = photoGangBanger.isActive;
            this.isPang = photoGangBanger.isPang;
            this.address = photoGangBanger.address;
            this.zipCode = photoGangBanger.zipCode;
            this.city = photoGangBanger.city;
            this.user = photoGangBanger.user;
        }
        database.photo_gang_bangers.add(photoGangBanger)
        return photoGangBanger
    }
}
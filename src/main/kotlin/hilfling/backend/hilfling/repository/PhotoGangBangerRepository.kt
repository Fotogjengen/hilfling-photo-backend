package hilfling.backend.hilfling.repository

import hilfling.backend.hilfing.model.PhotoGangBanger
import hilfling.backend.hilfing.model.User
import hilfling.backend.hilfing.model.photo_gang_bangers
import hilfling.backend.hilfing.model.users
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import org.springframework.beans.factory.annotation.Autowired

class PhotoGangBangerRepository {
    @Autowired
    lateinit var database: Database

    fun findById(id: Long): PhotoGangBanger? {
        return database.photo_gang_bangers.find{it.id eq  id}
    }

    fun findAll(): List<PhotoGangBanger> {
        return database.photo_gang_bangers.toList()
    }

    // TODO: Should we pass in Long or full object?
    fun createUser(
            relationshipStatus: String,
            semesterStart: String,
            isActive: Boolean,
            isPang: Boolean,
            address: String,
            zipCode: String,
            city: String,
            user: User

    ): PhotoGangBanger? {
        val userFromDatabase = database.users.find{ it.id eq user.id } ?: return null
        val photoGangBanger = PhotoGangBanger{
            this.relationshipStatus = relationshipStatus;
            this.semesterStart = semesterStart;
            this.isActive = isActive;
            this.isPang = isPang;
            this.address = address;
            this.zipCode = zipCode;
            this.city = city;
            this.user = user;
        }
        database.photo_gang_bangers.add(photoGangBanger)
        return photoGangBanger
    }
}
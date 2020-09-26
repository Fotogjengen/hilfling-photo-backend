package hilfling.backend.hilfling.repository;


import hilfling.backend.hilfing.model.*
import me.liuwj.ktorm.database.Database;
import org.springframework.beans.factory.annotation.Autowired;
import me.liuwj.ktorm.dsl.eq;
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList

class AlbumRepository {
    @Autowired
    lateinit var database: Database

    fun findById(id: Long): User? {
        return database.users.find { it.id eq id }
    }

    fun findAll(): List<User> {
        return database.users.toList()
    }

    fun create(
            firstName: String,
            lastName: String,
            username: String,
            email: String,
            profilePicture: String,
            phoneNumber: String,
            sex: String
    ): User {
        val user = User {
            this.firstName = firstName;
            this.lastName = lastName;
            this.username = username;
            this.email = email;
            this.profilePicture = profilePicture;
            this.phoneNumber = phoneNumber;
            this.sex = sex
        }
        database.users.add(user)
        return user
    }
}

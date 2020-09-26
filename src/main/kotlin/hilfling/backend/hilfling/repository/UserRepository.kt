package hilfling.backend.hilfling.repository;


import hilfling.backend.hilfling.model.*
import me.liuwj.ktorm.database.Database;
import org.springframework.beans.factory.annotation.Autowired;
import me.liuwj.ktorm.dsl.eq;
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList

class UserRepository {
    @Autowired
    lateinit var database: Database

    fun findById(id: Int): User? {
        return database.users.find { it.id eq id }
    }

    fun findAll(): List<User> {
        return database.users.toList()
    }

    fun create(
            user: User
    ): User {
        val userFromDatabase = User {
            this.firstName = user.firstName;
            this.lastName = user.lastName;
            this.username = user.username;
            this.email = user.email;
            this.profilePicture = user.profilePicture;
            this.phoneNumber = user.phoneNumber;
            this.sex = user.sex
        }
        database.users.add(userFromDatabase)
        return userFromDatabase
    }
}

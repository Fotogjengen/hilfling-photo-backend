package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.model.SamfundetUser
import no.fg.hilflingbackend.model.samfundet_users
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
open class SamfundetUserRepository {
  @Autowired
  open lateinit var database: Database

  fun findById(id: UUID): SamfundetUser? {
    return database.samfundet_users.find { it.id eq id }
  }

  fun findAll(): List<SamfundetUser> {
    return database.samfundet_users.toList()
  }

  fun create(
    samfundetUser: SamfundetUser
  ): SamfundetUser {
    val userFromDatabase = SamfundetUser {
      this.firstName = samfundetUser.firstName
      this.lastName = samfundetUser.lastName
      this.username = samfundetUser.username
      this.email = samfundetUser.email
      this.profilePicture = samfundetUser.profilePicture
      this.phoneNumber = samfundetUser.phoneNumber
      this.sex = samfundetUser.sex
    }
    database.samfundet_users.add(userFromDatabase)
    return userFromDatabase
  }
}

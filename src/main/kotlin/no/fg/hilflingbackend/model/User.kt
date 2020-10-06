package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.*
import no.fg.hilflingbackend.model.BaseModel
import no.fg.hilflingbackend.model.BaseTable
import no.fg.hilflingbackend.value_object.Email

interface User : BaseModel<User> {
    companion object : Entity.Factory<User>()

    var firstName: String
    var lastName: String
    var username: String
    var email: Email
    var profilePicture: String
    var phoneNumber: String
    var sex: String
}

object Users : BaseTable<User>("user") {
    val firstName = varchar("first_name").bindTo { it.firstName }
    val lastName = varchar("last_name").bindTo { it.lastName }
    val username = varchar("username").bindTo { it.username }
    val email = varchar("email").bindTo { it.email.value  }
    val profilePicture = varchar("profile_picture").bindTo { it.profilePicture }
    val phoneNumber = varchar("phone_number").bindTo { it.phoneNumber }
    val sex = varchar("sex").bindTo { it.sex }
}

val Database.users get() = this.sequenceOf(Users)
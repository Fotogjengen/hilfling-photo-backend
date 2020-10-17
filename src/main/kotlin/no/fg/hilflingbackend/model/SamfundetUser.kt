package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.*
import no.fg.hilflingbackend.value_object.Email

interface SamfundetUser : BaseModel<SamfundetUser> {
    companion object : Entity.Factory<SamfundetUser>()

    var firstName: String
    var lastName: String
    var username: String

    //var email: Email
    var email: String
    var profilePicture: String
    var phoneNumber: String
    var sex: String

    var securityLevel: SecurityLevel
}

object SamfundetUsers : BaseTable<SamfundetUser>("samfundet_user") {
    val firstName = varchar("first_name").bindTo { it.firstName }
    val lastName = varchar("last_name").bindTo { it.lastName }
    val username = varchar("username").bindTo { it.username }

    //val email = varchar("email").bindTo { it.email.value  }
    val email = varchar("email").bindTo { it.email }
    val profilePicture = varchar("profile_picture").bindTo { it.profilePicture }
    val phoneNumber = varchar("phone_number").bindTo { it.phoneNumber }
    val sex = varchar("sex").bindTo { it.sex }

    val securityLevelId = int("security_level_id").references(SecurityLevels) { it.securityLevel }
}

val Database.samfundet_users get() = this.sequenceOf(SamfundetUsers)
package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.boolean
import me.liuwj.ktorm.schema.varchar
import no.fg.hilflingbackend.dto.PhotoGangBangerDto
import no.fg.hilflingbackend.dto.PhotoGangBangerId
import no.fg.hilflingbackend.dto.RelationshipStatus
import no.fg.hilflingbackend.dto.SemesterStart

interface PhotoGangBanger : BaseModel<PhotoGangBanger> {
  companion object : Entity.Factory<PhotoGangBanger>()

  var relationshipStatus: String
  var semesterStart: String
  var isActive: Boolean
  var isPang: Boolean

  var firstName: String
  var lastName: String
  var username: String
  var email: String
  var profilePicture: String
  var phoneNumber: String
}

fun PhotoGangBanger.toDto(): PhotoGangBangerDto = PhotoGangBangerDto(
  photoGangBangerId = PhotoGangBangerId(this.id),
  relationShipStatus = RelationshipStatus.valueOf(this.relationshipStatus),
  semesterStart = SemesterStart(this.semesterStart),
  isActive = this.isActive,
  isPang = this.isPang,
  firstName = this.firstName,
  lastName = this.lastName,
  username = this.username,
  email = this.email,
  profilePicture = this.profilePicture,
  phoneNumber = this.phoneNumber,
)

object PhotoGangBangers : BaseTable<PhotoGangBanger>("photo_gang_banger") {
  val relationshipStatus = varchar("relationship_status").bindTo { it.relationshipStatus }
  val semesterStart = varchar("semester_start").bindTo { it.semesterStart }
  val isActive = boolean("is_active").bindTo { it.isActive }
  val isPang = boolean("is_pang").bindTo { it.isPang }

  val firstName = varchar("first_name").bindTo { it.firstName }
  val lastName = varchar("last_name").bindTo { it.lastName }
  val username = varchar("username").bindTo { it.username }
  val email = varchar("email").bindTo { it.email }
  val profilePicture = varchar("profile_picture").bindTo { it.profilePicture }
  val phoneNumber = varchar("phone_number").bindTo { it.phoneNumber }
}

val Database.photo_gang_bangers get() = this.sequenceOf(PhotoGangBangers)

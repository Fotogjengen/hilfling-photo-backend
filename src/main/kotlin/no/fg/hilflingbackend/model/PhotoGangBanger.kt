package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.boolean
import me.liuwj.ktorm.schema.date
import me.liuwj.ktorm.schema.uuid
import me.liuwj.ktorm.schema.varchar
import no.fg.hilflingbackend.dto.PhotoGangBangerDto
import no.fg.hilflingbackend.dto.PhotoGangBangerId
import no.fg.hilflingbackend.valueobject.SemesterStart
import java.time.LocalDate

interface PhotoGangBanger : BaseModel<PhotoGangBanger> {
  companion object : Entity.Factory<PhotoGangBanger>()

  var semesterStart: String
  var isActive: Boolean
  var isPang: Boolean

  var name: String
  var foodPreference: String?
  var birthday: LocalDate?
  var username: String
  var email: String
  var phoneNumber: String

  // Foreign keys
  var profilePicture: UserUpload?
}

fun PhotoGangBanger.toDto(): PhotoGangBangerDto {
  val dto =
    PhotoGangBangerDto(
      photoGangBangerId = PhotoGangBangerId(this.id),
      semesterStart = SemesterStart(this.semesterStart),
      isActive = this.isActive,
      isPang = this.isPang,
      name = this.name,
      foodPreference = this.foodPreference,
      birthday = this.birthday,
      username = this.username,
      email = this.email,
      phoneNumber = this.phoneNumber,
      profilePicture = null,
    )
  val profilePicture = this.profilePicture ?: return dto
  // Owner is passed explicitly to break the member <-> upload DTO cycle
  return dto.copy(
    profilePicture = profilePicture.toDto(owner = dto),
  )
}

object PhotoGangBangers : BaseTable<PhotoGangBanger>("photo_gang_banger") {
  val semesterStart = varchar("semester_start").bindTo { it.semesterStart }
  val isActive = boolean("is_active").bindTo { it.isActive }
  val isPang = boolean("is_pang").bindTo { it.isPang }

  val name = varchar("name").bindTo { it.name }
  val foodPreference = varchar("food_preference").bindTo { it.foodPreference }
  val birthday = date("birthday").bindTo { it.birthday }
  val username = varchar("username").bindTo { it.username }
  val email = varchar("email").bindTo { it.email }
  val phoneNumber = varchar("phone_number").bindTo { it.phoneNumber }

  // Foreign keys
  val profilePictureId = uuid("profile_picture").references(UserUploads) { it.profilePicture }
}

val Database.photo_gang_bangers
  get() = this.sequenceOf(PhotoGangBangers)

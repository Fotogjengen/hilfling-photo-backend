package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.uuid
import me.liuwj.ktorm.schema.varchar
import no.fg.hilflingbackend.dto.PhotoGangBangerDto
import no.fg.hilflingbackend.dto.SecurityLevelDto
import no.fg.hilflingbackend.dto.UserUploadDto
import no.fg.hilflingbackend.dto.UserUploadId
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import java.util.UUID

interface UserUpload : BaseModel<UserUpload> {
  companion object : Entity.Factory<UserUpload>()

  var securityLevel: String?
  var link: String?

  // Foreign keys
  // Plain FK column, not references(): PhotoGangBangers references UserUploads, and Ktorm
  // rejects mutual references between tables (circular reference check at class init).
  var photoGangBangerId: UUID
}

fun UserUpload.toDto(owner: PhotoGangBangerDto): UserUploadDto =
  UserUploadDto(
    userUploadId = UserUploadId(this.id),
    securityLevel = this.securityLevel?.let { SecurityLevelDto(SecurityLevelType.valueOf(it)) },
    link = this.link,
    photoGangBangerDto = owner,
    dateUploaded = this.dateCreated.toLocalDate(),
  )

object UserUploads : BaseTable<UserUpload>("user_uploads") {
  val securityLevel = varchar("security_level").bindTo { it.securityLevel }
  val link = varchar("link").bindTo { it.link }

  // Foreign keys
  val photoGangBangerId = uuid("photo_gang_banger_id").bindTo { it.photoGangBangerId }
}

val Database.user_uploads
  get() = this.sequenceOf(UserUploads)

package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.UserUpload
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class UserUploadRequestDto(
  val link: String,
  val securityLevel: SecurityLevelDto,
)

data class UserUploadDto(
  val userUploadId: UserUploadId = UserUploadId(),
  val securityLevel: SecurityLevelDto?,
  val link: String?,
  val photoGangBangerDto: PhotoGangBangerDto,
  val dateUploaded: LocalDate,
) {
  fun toEntity(): UserUpload {
    val userUpload = this
    return UserUpload {
      this.id = userUpload.userUploadId.id
      this.securityLevel = userUpload.securityLevel?.securityLevelType?.type
      this.link = userUpload.link
      this.dateCreated = LocalDateTime.now()
      this.photoGangBangerId = userUpload.photoGangBangerDto.photoGangBangerId.id
    }
  }
}

data class UserUploadId(
  override val id: UUID = UUID.randomUUID(),
) : UuidId {
  override fun toString(): String = id.toString()
}

package no.fg.hilflingbackend.service

import jakarta.persistence.EntityNotFoundException
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.PhotoGangBangerDto
import no.fg.hilflingbackend.dto.UserUploadDto
import no.fg.hilflingbackend.dto.UserUploadRequestDto
import no.fg.hilflingbackend.repository.PhotoGangBangerRepository
import no.fg.hilflingbackend.repository.UserUploadRepository
import no.fg.hilflingbackend.valueobject.Permission
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.UUID

@Service
class UserUploadService(
  val userUploadRepository: UserUploadRepository,
  val photoGangBangerRepository: PhotoGangBangerRepository,
) {
  fun register(
    request: UserUploadRequestDto,
    username: String,
  ): UserUploadDto {
    validate(request)
    val photoGangBanger =
      photoGangBangerRepository.findByUsername(username)
        ?: throw EntityNotFoundException("PhotoGangBanger for user '$username' not found")
    val userUpload =
      UserUploadDto(
        securityLevel = request.securityLevel,
        link = request.link,
        photoGangBangerDto = photoGangBanger,
        dateUploaded = LocalDate.now(),
      )
    return userUploadRepository.create(userUpload)
  }

  fun findById(
    userUploadId: UUID,
    userSecurityLevel: SecurityLevelType,
  ): UserUploadDto {
    val userUpload =
      userUploadRepository.findById(userUploadId)
        ?: throw EntityNotFoundException("UserUpload $userUploadId not found")
    if (effectiveSecurityLevel(userUpload).ordinal < userSecurityLevel.ordinal) {
      throw AccessDeniedException("Insufficient security level to access user upload $userUploadId")
    }
    return userUpload
  }

  fun findAll(
    page: Int,
    pageSize: Int,
    username: String,
    userPermissions: List<Permission>,
  ): Page<UserUploadDto> {
    val photoGangBanger = requirePhotoGangBanger(username)
    return if (Permission.ARCHIVE_MANAGE in userPermissions) {
      userUploadRepository.findAll(page, pageSize)
    } else {
      userUploadRepository.findByPhotoGangBangerId(photoGangBanger.photoGangBangerId.id, page, pageSize)
    }
  }

  fun delete(
    userUploadId: UUID,
    username: String,
    userPermissions: List<Permission>,
  ): UserUploadDto {
    val userUpload =
      userUploadRepository.findById(userUploadId)
        ?: throw EntityNotFoundException("UserUpload $userUploadId not found")
    val photoGangBanger = requirePhotoGangBanger(username)
    val isOwner = userUpload.photoGangBangerDto.photoGangBangerId.id == photoGangBanger.photoGangBangerId.id
    if (!isOwner && Permission.ARCHIVE_MANAGE !in userPermissions) {
      throw AccessDeniedException("Only the owner or users with the ARCHIVE_MANAGE permission can delete user uploads")
    }
    userUploadRepository.delete(userUploadId)
    return userUpload
  }

  private fun requirePhotoGangBanger(username: String): PhotoGangBangerDto =
    photoGangBangerRepository.findByUsername(username)
      ?: throw EntityNotFoundException("PhotoGangBanger for user '$username' not found")

  private fun validate(request: UserUploadRequestDto) {
    val errors = mutableListOf<String>()
    if (request.link.isBlank()) {
      errors.add("Link must not be blank")
    }
    if (request.link.length > MAX_LINK_LENGTH) {
      errors.add("Link must not exceed $MAX_LINK_LENGTH characters")
    }
    val securityLevel = request.securityLevel.securityLevelType
    if (securityLevel !in ALLOWED_SECURITY_LEVELS) {
      errors.add("Security level ${securityLevel.type} is not allowed for user uploads")
    }
    if (errors.isNotEmpty()) throw IllegalArgumentException(errors.joinToString(", "))
  }

  companion object {
    private const val MAX_LINK_LENGTH = 255
    private val ALLOWED_SECURITY_LEVELS = setOf(SecurityLevelType.FG, SecurityLevelType.ALLE)

    private fun effectiveSecurityLevel(userUpload: UserUploadDto): SecurityLevelType = userUpload.securityLevel?.securityLevelType ?: SecurityLevelType.FG
  }
}

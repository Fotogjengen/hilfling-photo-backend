package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.update
import me.liuwj.ktorm.entity.find
import no.fg.hilflingbackend.dto.SamfundetUserDto
import no.fg.hilflingbackend.dto.SamfundetUserId
import no.fg.hilflingbackend.dto.SamfundetUserPatchRequestDto
import no.fg.hilflingbackend.dto.SecurityLevelDto
import no.fg.hilflingbackend.dto.SecurityLevelId
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.SamfundetUser
import no.fg.hilflingbackend.model.SamfundetUsers
import no.fg.hilflingbackend.model.samfundet_users
import no.fg.hilflingbackend.value_object.Email
import no.fg.hilflingbackend.value_object.PhoneNumber
import org.springframework.stereotype.Repository
import jakarta.persistence.EntityNotFoundException

@Repository
open class SamfundetUserRepository(
  database: Database,
) : BaseRepository<SamfundetUser, SamfundetUserDto, SamfundetUserPatchRequestDto>(
    table = SamfundetUsers,
    database = database,
  ) {

  override fun convertToClass(qrs: QueryRowSet): SamfundetUserDto =
    SamfundetUserDto(
      samfundetUserId = SamfundetUserId(qrs[SamfundetUsers.id]!!),
      firstName = qrs[SamfundetUsers.firstName]!!,
      lastName = qrs[SamfundetUsers.lastName]!!,
      username = qrs[SamfundetUsers.username]!!,
      phoneNumber = PhoneNumber(qrs[SamfundetUsers.phoneNumber]!!),
      email = Email(qrs[SamfundetUsers.email]!!),
      profilePicturePath = qrs[SamfundetUsers.profilePicture]!!,
      sex = qrs[SamfundetUsers.sex]!!,
      securityLevel =
        SecurityLevelDto(
          securityLevelId =
            SecurityLevelId(
              qrs[SamfundetUsers.securityLevelId]!!,
            ),
        ),
    )

  override fun create(dto: SamfundetUserDto): Int = database.samfundet_users.add(dto.toEntity())

  override fun patch(dto: SamfundetUserPatchRequestDto): SamfundetUserDto {
    val fromDb =
      findById(dto.samfundetUserId.id)
        ?: throw EntityNotFoundException("Could not find SecurityLevel")
    val newDto =
      SamfundetUserDto(
        samfundetUserId = fromDb.samfundetUserId,
        firstName = dto.firstName ?: fromDb.firstName,
        lastName = dto.lastName ?: fromDb.lastName,
        username = dto.username ?: fromDb.username,
        phoneNumber = dto.phoneNumber ?: fromDb.phoneNumber,
        email = dto.email ?: fromDb.email,
        profilePicturePath =
          dto.profilePicturePath ?: fromDb.profilePicturePath,
        sex = dto.sex ?: fromDb.sex,
        securityLevel = dto.securityLevel ?: fromDb.securityLevel,
      )
    val updated = database.samfundet_users.update(newDto.toEntity())
    return if (updated == 1) newDto else fromDb
  }
}

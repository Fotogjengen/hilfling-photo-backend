package no.fg.hilflingbackend.repository

import jakarta.persistence.EntityNotFoundException
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.dto.ExternalUserDto
import no.fg.hilflingbackend.dto.ExternalUserId
import no.fg.hilflingbackend.dto.ExternalUserPatchRequestDto
import no.fg.hilflingbackend.dto.SecurityLevelDto
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.ExternalUser
import no.fg.hilflingbackend.model.ExternalUsers
import no.fg.hilflingbackend.model.external_users
import no.fg.hilflingbackend.model.toDto
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Repository

@Repository
open class ExternalUserRepository(
  database: Database,
  val passwordEncoder: PasswordEncoder,
) : BaseRepository<ExternalUser, ExternalUserDto, ExternalUserPatchRequestDto>(table = ExternalUsers, database = database) {
  override fun convertToClass(qrs: QueryRowSet): ExternalUserDto =
    ExternalUserDto(
      externalUserId = ExternalUserId(qrs[ExternalUsers.id]!!),
      username = qrs[ExternalUsers.username]!!,
      password = qrs[ExternalUsers.password],
      email = qrs[ExternalUsers.email],
      fullName = qrs[ExternalUsers.fullName],
      securityLevel = qrs[ExternalUsers.securityLevel]?.let { SecurityLevelDto(SecurityLevelType.valueOf(it)) },
      description = qrs[ExternalUsers.description],
      isActive = qrs[ExternalUsers.isActive]!!,
    )

  override fun create(dto: ExternalUserDto): Int {
    val hashedDto =
      dto.copy(
        password = dto.password?.let { passwordEncoder.encode(it) },
      )
    return database.external_users.add(hashedDto.toEntity())
  }

  fun findByUsername(username: String): ExternalUserDto? =
    database
      .external_users
      .find {
        it.username eq username
      }?.toDto()

  override fun patch(dto: ExternalUserPatchRequestDto): ExternalUserDto {
    val fromDb =
      findById(dto.externalUserId.id)
        ?: throw EntityNotFoundException("Could not find ExternalUser")
    val newDto =
      ExternalUserDto(
        externalUserId = fromDb.externalUserId,
        username = dto.username ?: fromDb.username,
        password = dto.password?.let { passwordEncoder.encode(it) } ?: fromDb.password,
        email = dto.email ?: fromDb.email,
        fullName = dto.fullName ?: fromDb.fullName,
        securityLevel = dto.securityLevel ?: fromDb.securityLevel,
        description = dto.description ?: fromDb.description,
        isActive = dto.isActive ?: fromDb.isActive,
      )
    val updated = database.external_users.update(newDto.toEntity())

    return if (updated == 1) newDto else fromDb
  }
}

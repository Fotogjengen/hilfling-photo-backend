package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.dto.SecurityLevelDto
import no.fg.hilflingbackend.dto.SecurityLevelId
import no.fg.hilflingbackend.dto.SecurityLevelPatchRequestDto
import no.fg.hilflingbackend.model.SecurityLevel
import no.fg.hilflingbackend.model.SecurityLevels
import no.fg.hilflingbackend.model.security_levels
import no.fg.hilflingbackend.value_object.SecurityLevelType
import org.springframework.stereotype.Repository
import javax.persistence.EntityNotFoundException

@Repository
open class SecurityLevelRepository(database: Database) : BaseRepository<SecurityLevel, SecurityLevelDto, SecurityLevelPatchRequestDto>(table = SecurityLevels, database = database) {
  override fun convertToClass(qrs: QueryRowSet): SecurityLevelDto = SecurityLevelDto(
    securityLevelId = SecurityLevelId(qrs[SecurityLevels.id]!!),
    securityLevelType = SecurityLevelType.valueOf(qrs[SecurityLevels.type]!!)
  )

  override fun create(dto: SecurityLevelDto): Int {
    return database.security_levels.add(dto.toEntity())
  }

  override fun patch(dto: SecurityLevelPatchRequestDto): SecurityLevelDto {
    val fromDb = findById(dto.securityLevelId.id)
      ?: throw EntityNotFoundException("Could not find SecurityLevel")
    val newDto = SecurityLevelDto(
      securityLevelId = fromDb.securityLevelId,
      securityLevelType = dto.securityLevelType ?: fromDb.securityLevelType
    )
    val updated = database.security_levels.update(newDto.toEntity())

    return if (updated == 1) newDto else fromDb
  }
}

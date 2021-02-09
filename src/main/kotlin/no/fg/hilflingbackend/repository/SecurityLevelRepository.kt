package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.dto.PurchaseOrderDto
import no.fg.hilflingbackend.dto.SecurityLevelDto
import no.fg.hilflingbackend.dto.SecurityLevelId
import no.fg.hilflingbackend.model.SecurityLevel
import no.fg.hilflingbackend.model.SecurityLevels
import no.fg.hilflingbackend.model.security_levels
import no.fg.hilflingbackend.value_object.SecurityLevelType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.UUID


@Repository
open class SecurityLevelRepository(database: Database) : BaseRepository<SecurityLevel, SecurityLevelDto>(table = SecurityLevels, database = database) {
  override fun convertToClass(qrs: QueryRowSet): SecurityLevelDto = SecurityLevelDto(
    securityLevelId = SecurityLevelId(qrs[SecurityLevels.id]!!),
    securityLevelType = SecurityLevelType.valueOf(qrs[SecurityLevels.type]!!),
  )

  override fun create(dto: SecurityLevelDto): Int {
    return database.security_levels.add(dto.toEntity())
  }

  override fun patch(dto: SecurityLevelDto): Int {
    return database.security_levels.update(dto.toEntity())
  }
}

package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.dto.GangDto
import no.fg.hilflingbackend.dto.GangId
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.Gang
import no.fg.hilflingbackend.model.Gangs
import no.fg.hilflingbackend.model.gangs
import org.springframework.stereotype.Repository

@Repository
open class GangRepository(database: Database) : BaseRepository<Gang, GangDto>(table = Gangs, database = database) {
  override fun convertToClass(qrs: QueryRowSet): GangDto = GangDto(
    gangId = GangId(qrs[Gangs.id]!!),
    name = qrs[Gangs.name]
  )

  override fun create(dto: GangDto): Int {
    return database.gangs.add(dto.toEntity())
  }

  fun patch(dto: GangDto): Int {
    return database.gangs.update(dto.toEntity())
  }
}

package no.fg.hilflingbackend.repository

import jakarta.persistence.EntityNotFoundException
import no.fg.hilflingbackend.dto.GangDto
import no.fg.hilflingbackend.dto.GangId
import no.fg.hilflingbackend.dto.GangPatchRequestDto
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.Gang
import no.fg.hilflingbackend.model.Gangs
import no.fg.hilflingbackend.model.gangs
import org.ktorm.database.Database
import org.ktorm.dsl.QueryRowSet
import org.ktorm.entity.add
import org.ktorm.entity.update
import org.springframework.stereotype.Repository

@Repository
open class GangRepository(database: Database) : BaseRepository<Gang, GangDto, GangPatchRequestDto>(table = Gangs, database = database) {
  override fun convertToClass(qrs: QueryRowSet): GangDto = GangDto(
    gangId = GangId(qrs[Gangs.id]!!),
    name = qrs[Gangs.name]!!
  )

  override fun create(dto: GangDto): Int {
    return database.gangs.add(dto.toEntity())
  }

  override fun patch(dto: GangPatchRequestDto): GangDto {
    val fromDb = findById(dto.gangId.id)
      ?: throw EntityNotFoundException("Could not find Gang")
    val newDto = GangDto(
      gangId = fromDb.gangId,
      name = dto.name ?: fromDb.name
    )
    val updated = database.gangs.update(newDto.toEntity())

    return if (updated == 1) newDto else fromDb
  }
}

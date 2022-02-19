package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.dto.PositionDto
import no.fg.hilflingbackend.dto.PositionId
import no.fg.hilflingbackend.dto.PositionPatchRequestDto
import no.fg.hilflingbackend.dto.SecurityLevelDto
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.Position
import no.fg.hilflingbackend.model.Positions
import no.fg.hilflingbackend.model.positions
import no.fg.hilflingbackend.model.security_levels
import no.fg.hilflingbackend.value_object.Email
import org.springframework.stereotype.Repository
import javax.persistence.EntityNotFoundException

@Repository
open class PositionRepository(database: Database) : BaseRepository<Position, PositionDto, PositionPatchRequestDto>(table = Positions, database = database) {
  override fun convertToClass(qrs: QueryRowSet): PositionDto = PositionDto(
    positionId = PositionId(qrs[Positions.id]!!),
    title = qrs[Positions.title]!!,
    email = Email(qrs[Positions.email]!!)
  )

  override fun create(dto: PositionDto): Int {
    return database.positions.add(dto.toEntity())
  }

  override fun patch(dto: PositionPatchRequestDto): PositionDto {
    val fromDb = findById(dto.positionId.id)
      ?: throw EntityNotFoundException("Could not find Position")
    val newDto = PositionDto(
      positionId = fromDb.positionId,
      title = dto.title ?: fromDb.title,
      email = dto.email ?: fromDb.email
    )
    val updated = database.positions.update(newDto.toEntity())

    return if (updated == 1) newDto else fromDb
  }
}

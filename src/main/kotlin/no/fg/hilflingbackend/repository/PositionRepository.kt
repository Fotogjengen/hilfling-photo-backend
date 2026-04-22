package no.fg.hilflingbackend.repository

import jakarta.persistence.EntityNotFoundException
import no.fg.hilflingbackend.dto.PositionDto
import no.fg.hilflingbackend.dto.PositionId
import no.fg.hilflingbackend.dto.PositionPatchRequestDto
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.Position
import no.fg.hilflingbackend.model.Positions
import no.fg.hilflingbackend.model.positions
import org.ktorm.database.Database
import org.ktorm.dsl.QueryRowSet
import org.ktorm.entity.add
import org.ktorm.entity.update
import org.springframework.stereotype.Repository

@Repository
open class PositionRepository(database: Database) : BaseRepository<Position, PositionDto, PositionPatchRequestDto>(table = Positions, database = database) {
  override fun convertToClass(qrs: QueryRowSet): PositionDto = PositionDto(
    positionId = PositionId(qrs[Positions.id]!!),
    title = qrs[Positions.title]!!,
    email = qrs[Positions.email]!!,
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

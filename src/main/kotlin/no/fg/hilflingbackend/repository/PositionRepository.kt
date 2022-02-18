package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.dto.PositionDto
import no.fg.hilflingbackend.dto.PositionId
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.Position
import no.fg.hilflingbackend.model.Positions
import no.fg.hilflingbackend.model.positions
import no.fg.hilflingbackend.value_object.Email
import org.springframework.stereotype.Repository

@Repository
open class PositionRepository(database: Database) : BaseRepository<Position, PositionDto>(table = Positions, database = database) {
  override fun convertToClass(qrs: QueryRowSet): PositionDto = PositionDto(
    positionId = PositionId(qrs[Positions.id]!!),
    title = qrs[Positions.title]!!,
    email = Email(qrs[Positions.email]!!)
  )

  override fun create(dto: PositionDto): Int {
    return database.positions.add(dto.toEntity())
  }

  fun patch(dto: PositionDto): Int {
    return database.positions.update(dto.toEntity())
  }
}

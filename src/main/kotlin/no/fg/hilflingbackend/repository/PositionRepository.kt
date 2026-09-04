package no.fg.hilflingbackend.repository

import jakarta.persistence.EntityNotFoundException
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.delete
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.from
import me.liuwj.ktorm.dsl.insert
import me.liuwj.ktorm.dsl.map
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.where
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.any
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.dto.PositionDto
import no.fg.hilflingbackend.dto.PositionId
import no.fg.hilflingbackend.dto.PositionPatchRequestDto
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.Position
import no.fg.hilflingbackend.model.PositionToPermissions
import no.fg.hilflingbackend.model.Positions
import no.fg.hilflingbackend.model.positions
import no.fg.hilflingbackend.valueobject.Email
import no.fg.hilflingbackend.valueobject.Permission
import org.springframework.stereotype.Repository

@Repository
open class PositionRepository(
  database: Database,
) : BaseRepository<Position, PositionDto, PositionPatchRequestDto>(
    table = Positions,
    database = database,
  ) {
  override fun convertToClass(qrs: QueryRowSet): PositionDto =
    PositionDto(
      positionId = PositionId(qrs[Positions.id]!!),
      title = qrs[Positions.title]!!,
      email = Email(qrs[Positions.email]!!),
    )

  override fun create(dto: PositionDto): Int {
    if (database.positions.any { it.id eq dto.positionId.id }) return 0
    return database.positions.add(dto.toEntity())
  }

  override fun patch(dto: PositionPatchRequestDto): PositionDto {
    val fromDb =
      findById(dto.positionId.id) ?: throw EntityNotFoundException("Could not find Position")
    val newDto =
      PositionDto(
        positionId = fromDb.positionId,
        title = dto.title ?: fromDb.title,
        email = dto.email ?: fromDb.email,
      )
    val updated = database.positions.update(newDto.toEntity())

    return if (updated == 1) newDto else fromDb
  }

  fun findPermissionsByPositionId(positionId: PositionId): List<Permission> =
    database
      .from(PositionToPermissions)
      .select(PositionToPermissions.permission)
      .where { PositionToPermissions.positionId eq positionId.id }
      .map { row ->
        runCatching { Permission.valueOf(row[PositionToPermissions.permission]!!) }.getOrNull()
      }.filterNotNull()

  fun addPermissionToPosition(
    positionId: PositionId,
    permission: Permission,
  ): Int =
    database.insert(PositionToPermissions) {
      set(it.positionId, positionId.id)
      set(it.permission, permission.name)
    }

  fun removePermissionFromPosition(
    positionId: PositionId,
    permission: Permission,
  ): Int =
    database.delete(PositionToPermissions) {
      (it.positionId eq positionId.id) and (it.permission eq permission.name)
    }
}

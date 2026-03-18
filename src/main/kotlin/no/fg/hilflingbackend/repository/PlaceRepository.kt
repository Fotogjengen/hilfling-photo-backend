package no.fg.hilflingbackend.repository

import org.ktorm.database.Database
import org.ktorm.dsl.QueryRowSet
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.ktorm.entity.update
import no.fg.hilflingbackend.dto.PlaceDto
import no.fg.hilflingbackend.dto.PlaceId
import no.fg.hilflingbackend.dto.PlacePatchRequestDto
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.Place
import no.fg.hilflingbackend.model.Places
import no.fg.hilflingbackend.model.places
import no.fg.hilflingbackend.model.toDto
import org.springframework.stereotype.Repository
import jakarta.persistence.EntityNotFoundException

@Repository
open class PlaceRepository(database: Database) : BaseRepository<Place, PlaceDto, PlacePatchRequestDto>(table = Places, database = database) {
  override fun convertToClass(qrs: QueryRowSet): PlaceDto = PlaceDto(
    placeId = PlaceId(qrs[Places.id]!!),
    name = qrs[Places.name]!!

  )

  override fun create(dto: PlaceDto): Int {
    return database.places.add(dto.toEntity())
  }

  fun findByName(name: String): PlaceDto? = database
    .places
    .find {
      it.name eq name
    }
    ?.toDto()

  override fun patch(dto: PlacePatchRequestDto): PlaceDto {
    val fromDb = findById(dto.placeId.id)
      ?: throw EntityNotFoundException("Could not find Place")
    val newDto = PlaceDto(
      placeId = fromDb.placeId,
      name = dto.name ?: fromDb.name
    )
    val updated = database.places.update(newDto.toEntity())

    return if (updated == 1) newDto else fromDb
  }
}

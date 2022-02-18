package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.dto.PlaceDto
import no.fg.hilflingbackend.dto.PlaceId
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.Place
import no.fg.hilflingbackend.model.Places
import no.fg.hilflingbackend.model.places
import no.fg.hilflingbackend.model.toDto
import org.springframework.stereotype.Repository

@Repository
open class PlaceRepository(database: Database) : BaseRepository<Place, PlaceDto>(table = Places, database = database) {
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

  fun patch(dto: PlaceDto): Int {
    return database.places.update(dto.toEntity())
  }
}

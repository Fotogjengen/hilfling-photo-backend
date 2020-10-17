package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.model.Place
import no.fg.hilflingbackend.model.places
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
open class PlaceRepository {
  @Autowired
  open lateinit var database: Database

  fun findById(id: UUID): Place? {
    return database.places.find { it.id eq id }
  }

  fun findAll(): List<Place> {
    return database.places.toList()
  }

  fun create(place: Place): Place {
    val placeFromDatabase = Place {
      this.name = place.name
    }
    database.places.add(placeFromDatabase)
    return placeFromDatabase
  }
}

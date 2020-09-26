package hilfling.backend.hilfling.repository

import hilfling.backend.hilfling.model.Place
import hilfling.backend.hilfling.model.places
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import org.springframework.beans.factory.annotation.Autowired

class PlaceRepository {
    @Autowired
    lateinit var database: Database

    fun findById(id: Int): Place? {
        return database.places.find{it.id eq  id}
    }

    fun findAll(): List<Place> {
        return database.places.toList()
    }

    fun create(place: Place): Place {
        val placeFromDatabase = Place{
            this.name = place.name;
        }
        database.places.add(placeFromDatabase)
        return placeFromDatabase
    }
}
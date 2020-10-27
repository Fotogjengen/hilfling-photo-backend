package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.dto.PhotoGangBangerDto
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.PhotoGangBanger
import no.fg.hilflingbackend.model.photo_gang_bangers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
open class PhotoGangBangerRepository {
  // TODO: Join with PhotoGangBangerPositions
  @Autowired
  open lateinit var database: Database

  fun findById(id: UUID): PhotoGangBanger? {
    return database.photo_gang_bangers.find { it.id eq id }
  }

  fun findAll(): List<PhotoGangBanger> {
    return database.photo_gang_bangers.toList()
  }

  fun findAllActives(): List<PhotoGangBanger> {
    return database.photo_gang_bangers.filter {
      it.isActive eq true
      it.isPang eq false
    }.toList()
  }

  fun findAllActivePangs(): List<PhotoGangBanger> {
    return database.photo_gang_bangers.filter {
      it.isActive eq true
      it.isPang eq true
    }.toList()
  }

  fun findAllInActivePangs(): List<PhotoGangBanger> {
    return database.photo_gang_bangers.filter {
      it.isActive eq false
      it.isPang eq true
    }.toList()
  }

  fun create(
    photoGangBangerDto: PhotoGangBangerDto
  ): Int =
    database.photo_gang_bangers.add(
      photoGangBangerDto.toEntity()
    )
}

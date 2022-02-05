package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.*
import no.fg.hilflingbackend.dto.PhotoGangBangerDto
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.photo_gang_bangers
import no.fg.hilflingbackend.model.toDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
open class PhotoGangBangerRepository {
  // TODO: Join with PhotoGangBangerDtoPositions
  @Autowired
  open lateinit var database: Database

  fun findById(id: UUID): PhotoGangBangerDto? {
    return database.photo_gang_bangers.find { it.id eq id }?.toDto()
  }

  fun findAll(): List<PhotoGangBangerDto> {
    return database.photo_gang_bangers
      .toList()
      .map { it.toDto() }
  }

  fun findAllActives(): List<PhotoGangBangerDto> {
    return database.photo_gang_bangers.filter {
      it.isActive eq true
      it.isPang eq false
    }.toList()
      .map { it.toDto() }
  }

  fun findAllActivePangs(): List<PhotoGangBangerDto> {
    return database.photo_gang_bangers.filter {
      it.isActive eq true
      it.isPang eq true
    }.toList()
      .map { it.toDto() }
  }

  fun findAllInActivePangs(): List<PhotoGangBangerDto> {
    return database.photo_gang_bangers.filter {
      it.isActive eq false
      it.isPang eq true
    }.toList()
      .map { it.toDto() }
  }

  fun create(
    dto: PhotoGangBangerDto
  ): Int =
    database.photo_gang_bangers.add(
      dto.toEntity()
    )

  fun patch(
    dto: PhotoGangBangerDto
  ): Int = database.photo_gang_bangers.update(
      dto.toEntity()
    )
}

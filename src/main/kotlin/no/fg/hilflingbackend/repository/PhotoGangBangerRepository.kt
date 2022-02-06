package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.PhotoGangBangerDto
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.photo_gang_bangers
import no.fg.hilflingbackend.model.toDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.UUID

interface IPhotoGangBangerRepository {
  fun findById(id: UUID): PhotoGangBangerDto?
  fun findAll(page: Int = 0, pageSize: Int = 100): Page<PhotoGangBangerDto>
  fun findAllActives(page: Int = 0, pageSize: Int = 100): Page<PhotoGangBangerDto>
  fun findAllActivePangs(page: Int = 0, pageSize: Int = 100): Page<PhotoGangBangerDto>
  fun findAllInactivePangs(page: Int = 0, pageSize: Int = 100): Page<PhotoGangBangerDto>
}

@Repository
class PhotoGangBangerRepository : IPhotoGangBangerRepository {
  // TODO: Join with PhotoGangBangerDtoPositions
  @Autowired
  open lateinit var database: Database

  override fun findById(id: UUID): PhotoGangBangerDto? {
    return database.photo_gang_bangers.find { it.id eq id }?.toDto()
  }

  override fun findAll(page: Int, pageSize: Int): Page<PhotoGangBangerDto> {
    val photoGangBangers = database.photo_gang_bangers
    val photoGangBangerDtos = photoGangBangers.toList()
    .map { it.toDto() }

    return Page(
      page = page,
      pageSize = pageSize,
      totalRecords = photoGangBangers.totalRecords,
      currentList = photoGangBangerDtos
    )
  }

  override fun findAllActives(page: Int, pageSize: Int): Page<PhotoGangBangerDto> {
    val photoGangBangers = database.photo_gang_bangers.filter {
      it.isActive eq true
      it.isPang eq false
    }
    val photoGangBangerDtos = photoGangBangers.toList()
      .map { it.toDto() }

    return Page(
      page = page,
      pageSize = pageSize,
      totalRecords = photoGangBangers.totalRecords,
      currentList = photoGangBangerDtos
    )
  }

  override fun findAllActivePangs(page: Int, pageSize: Int): Page<PhotoGangBangerDto> {
    val photoGangBangers = database.photo_gang_bangers.filter {
      it.isActive eq true
      it.isPang eq true
    }
    val photoGangBangerDtos = photoGangBangers.toList()
      .map { it.toDto() }

    return Page(
      page = page,
      pageSize = pageSize,
      totalRecords = photoGangBangers.totalRecords,
      currentList = photoGangBangerDtos
    )
  }

  override fun findAllInactivePangs(page: Int, pageSize: Int): Page<PhotoGangBangerDto> {
    val photoGangBangers = database.photo_gang_bangers.filter {
      it.isActive eq false
      it.isPang eq true
    }
    val photoGangBangerDtos = photoGangBangers.toList()
      .map { it.toDto() }

    return Page(
      page = page,
      pageSize = pageSize,
      totalRecords = photoGangBangers.totalRecords,
      currentList = photoGangBangerDtos
    )
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

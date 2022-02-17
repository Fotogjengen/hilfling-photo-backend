package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.insert
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.PhotoGangBangerDto
import no.fg.hilflingbackend.dto.PhotoGangBangerPatchRequestDto
import no.fg.hilflingbackend.dto.SamfundetUserDto
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.exceptions.EntityCreationException
import no.fg.hilflingbackend.exceptions.EntityExistsException
import no.fg.hilflingbackend.model.PhotoGangBangers
import no.fg.hilflingbackend.model.photo_gang_bangers
import no.fg.hilflingbackend.model.samfundet_users
import no.fg.hilflingbackend.model.toDto
import org.springframework.stereotype.Repository
import java.util.UUID
import javax.persistence.EntityNotFoundException

interface IPhotoGangBangerRepository {
  fun findById(id: UUID): PhotoGangBangerDto?
  fun findAll(page: Int = 0, pageSize: Int = 100): Page<PhotoGangBangerDto>
  fun findAllActives(page: Int = 0, pageSize: Int = 100): Page<PhotoGangBangerDto>
  fun findAllActivePangs(page: Int = 0, pageSize: Int = 100): Page<PhotoGangBangerDto>
  fun findAllInactivePangs(page: Int = 0, pageSize: Int = 100): Page<PhotoGangBangerDto>
}

@Repository
class PhotoGangBangerRepository(
  val database: Database
) : IPhotoGangBangerRepository {
  // TODO: Join with PhotoGangBangerDtoPositions

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
  ): Int {
    val existingPhotoGangBanger = database.photo_gang_bangers
      .find {
        it.samfundetUserId eq dto.samfundetUser.samfundetUserId.id
      }
    if (existingPhotoGangBanger != null) {
      throw EntityExistsException("PhotoGangBanger already exists")
    }

    val samfundetUser = database.samfundet_users.find {
      it.id eq dto.samfundetUser.samfundetUserId.id
    }
    if (samfundetUser == null) {
      try {
        database.samfundet_users.add(dto.samfundetUser.toEntity())
      } catch (_: Error) {
        throw EntityCreationException("Could not create new SamfundetUser")
      }
    }

    val created = database.insert(PhotoGangBangers) {
      set(it.id, dto.photoGangBangerId.id)
      set(it.isActive, dto.isActive)
      set(it.isPang, dto.isPang)
      set(it.address, dto.address)
      set(it.city, dto.city)
      set(it.positionId, dto.position.positionId.id)
      set(it.relationshipStatus, dto.relationShipStatus.status)
      set(it.samfundetUserId, dto.samfundetUser.samfundetUserId.id)
      set(it.semesterStart, dto.semesterStart.value)
      set(it.zipCode, dto.zipCode)
    }

    return created
  }


  fun patch(
    dto: PhotoGangBangerPatchRequestDto
  ): PhotoGangBangerDto? {

    val photoGangBangerDtoFromDb = findById(dto.photoGangBangerId.id)
        ?: throw EntityNotFoundException("Could not find PhotoGangBanger")

    println(photoGangBangerDtoFromDb.samfundetUser.samfundetUserId.id)

    var hasUpdatedSamfundetUser = 0
    if (dto.samfundetUser != null) {
      hasUpdatedSamfundetUser = database.samfundet_users.update(dto.samfundetUser.toEntity())
    }

    val photoGangBangerDto = PhotoGangBangerDto(
      photoGangBangerId = photoGangBangerDtoFromDb.photoGangBangerId,
      samfundetUser = dto.samfundetUser ?: photoGangBangerDtoFromDb.samfundetUser,
      city = dto.city ?: photoGangBangerDtoFromDb.city,
      zipCode = dto.zipCode ?: photoGangBangerDtoFromDb.zipCode,
      address = dto.address ?: photoGangBangerDtoFromDb.address,
      isPang = dto.isPang ?: photoGangBangerDtoFromDb.isPang,
      isActive = dto.isActive ?: photoGangBangerDtoFromDb.isActive,
      semesterStart = dto.semesterStart ?: photoGangBangerDtoFromDb.semesterStart,
      relationShipStatus = dto.relationshipStatus ?: photoGangBangerDtoFromDb.relationShipStatus,
      position = dto.position ?: photoGangBangerDtoFromDb.position
    )

    val hasUpdatedPhotoGangBanger = database.photo_gang_bangers.update(
      photoGangBangerDto.toEntity()
    )
    return findById(dto.photoGangBangerId.id)
  }
}

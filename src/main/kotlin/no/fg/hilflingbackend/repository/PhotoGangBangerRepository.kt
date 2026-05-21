package no.fg.hilflingbackend.repository

import jakarta.persistence.EntityNotFoundException
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
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.exceptions.EntityExistsException
import no.fg.hilflingbackend.model.PhotoGangBangers
import no.fg.hilflingbackend.model.photo_gang_bangers
import no.fg.hilflingbackend.model.toDto
import org.springframework.stereotype.Repository
import java.util.UUID

interface IPhotoGangBangerRepository {
  fun findById(id: UUID): PhotoGangBangerDto?

  fun findAll(
    page: Int = 0,
    pageSize: Int = 100,
  ): Page<PhotoGangBangerDto>

  fun findAllActives(
    page: Int = 0,
    pageSize: Int = 100,
  ): Page<PhotoGangBangerDto>

  fun findAllActivePangs(
    page: Int = 0,
    pageSize: Int = 100,
  ): Page<PhotoGangBangerDto>

  fun findAllInactivePangs(
    page: Int = 0,
    pageSize: Int = 100,
  ): Page<PhotoGangBangerDto>
}

@Repository
class PhotoGangBangerRepository(
  val database: Database,
) : IPhotoGangBangerRepository {
  override fun findById(id: UUID): PhotoGangBangerDto? = database.photo_gang_bangers.find { it.id eq id }?.toDto()

  override fun findAll(
    page: Int,
    pageSize: Int,
  ): Page<PhotoGangBangerDto> {
    val photoGangBangers = database.photo_gang_bangers
    val photoGangBangerDtos = photoGangBangers.toList().map { it.toDto() }
    return Page(page = page, pageSize = pageSize, totalRecords = photoGangBangers.totalRecords, currentList = photoGangBangerDtos)
  }

  override fun findAllActives(
    page: Int,
    pageSize: Int,
  ): Page<PhotoGangBangerDto> {
    val photoGangBangers =
      database.photo_gang_bangers.filter {
        it.isActive eq true
        it.isPang eq false
      }
    return Page(page = page, pageSize = pageSize, totalRecords = photoGangBangers.totalRecords, currentList = photoGangBangers.toList().map { it.toDto() })
  }

  override fun findAllActivePangs(
    page: Int,
    pageSize: Int,
  ): Page<PhotoGangBangerDto> {
    val photoGangBangers =
      database.photo_gang_bangers.filter {
        it.isActive eq true
        it.isPang eq true
      }
    return Page(page = page, pageSize = pageSize, totalRecords = photoGangBangers.totalRecords, currentList = photoGangBangers.toList().map { it.toDto() })
  }

  override fun findAllInactivePangs(
    page: Int,
    pageSize: Int,
  ): Page<PhotoGangBangerDto> {
    val photoGangBangers =
      database.photo_gang_bangers.filter {
        it.isActive eq false
        it.isPang eq true
      }
    return Page(page = page, pageSize = pageSize, totalRecords = photoGangBangers.totalRecords, currentList = photoGangBangers.toList().map { it.toDto() })
  }

  fun create(dto: PhotoGangBangerDto): Int {
    val existing = database.photo_gang_bangers.find { it.username eq dto.username }
    if (existing != null) throw EntityExistsException("PhotoGangBanger already exists")

    return database.insert(PhotoGangBangers) {
      set(it.id, dto.photoGangBangerId.id)
      set(it.isActive, dto.isActive)
      set(it.isPang, dto.isPang)
      set(it.semesterStart, dto.semesterStart.value)
      set(it.firstName, dto.firstName)
      set(it.lastName, dto.lastName)
      set(it.username, dto.username)
      set(it.email, dto.email)
      set(it.profilePicture, dto.profilePicture)
      set(it.phoneNumber, dto.phoneNumber)
    }
  }

  fun patch(dto: PhotoGangBangerPatchRequestDto): PhotoGangBangerDto? {
    val fromDb =
      findById(dto.photoGangBangerId.id)
        ?: throw EntityNotFoundException("Could not find PhotoGangBanger")

    val updated =
      PhotoGangBangerDto(
        photoGangBangerId = fromDb.photoGangBangerId,
        semesterStart = dto.semesterStart ?: fromDb.semesterStart,
        isActive = dto.isActive ?: fromDb.isActive,
        isPang = dto.isPang ?: fromDb.isPang,
        firstName = dto.firstName ?: fromDb.firstName,
        lastName = dto.lastName ?: fromDb.lastName,
        username = dto.username ?: fromDb.username,
        email = dto.email ?: fromDb.email,
        profilePicture = dto.profilePicture ?: fromDb.profilePicture,
        phoneNumber = dto.phoneNumber ?: fromDb.phoneNumber,
      )

    database.photo_gang_bangers.update(updated.toEntity())
    return findById(dto.photoGangBangerId.id)
  }

  fun findByUsername(username: String): PhotoGangBangerDto? = database.photo_gang_bangers.find { it.username eq username }?.toDto()
}

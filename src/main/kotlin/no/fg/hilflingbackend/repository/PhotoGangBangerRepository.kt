package no.fg.hilflingbackend.repository

import jakarta.persistence.EntityNotFoundException
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.desc
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.from
import me.liuwj.ktorm.dsl.innerJoin
import me.liuwj.ktorm.dsl.insert
import me.liuwj.ktorm.dsl.map
import me.liuwj.ktorm.dsl.orderBy
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.where
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.dto.MemberPositionDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.PhotoGangBangerDto
import no.fg.hilflingbackend.dto.PhotoGangBangerPatchRequestDto
import no.fg.hilflingbackend.dto.PositionId
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.exceptions.EntityExistsException
import no.fg.hilflingbackend.model.PhotoGangBangerToPositions
import no.fg.hilflingbackend.model.PhotoGangBangers
import no.fg.hilflingbackend.model.Positions
import no.fg.hilflingbackend.model.photo_gang_bangers
import no.fg.hilflingbackend.model.toDto
import no.fg.hilflingbackend.valueobject.Email
import no.fg.hilflingbackend.valueobject.SemesterStart
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
  private fun findPositionsForMember(memberId: UUID): List<MemberPositionDto> =
    database
      .from(PhotoGangBangerToPositions)
      .innerJoin(Positions, on = PhotoGangBangerToPositions.positionId eq Positions.id)
      .select(Positions.id, Positions.title, Positions.email, PhotoGangBangerToPositions.semesterStart, PhotoGangBangerToPositions.semesterEnd)
      .where { PhotoGangBangerToPositions.photoGangBangerId eq memberId }
      .orderBy(PhotoGangBangerToPositions.semesterStart.desc())
      .map { row ->
        MemberPositionDto(
          positionId = PositionId(row[Positions.id]!!),
          title = row[Positions.title]!!,
          email = Email(row[Positions.email]!!),
          semesterStart = SemesterStart(row[PhotoGangBangerToPositions.semesterStart]!!),
          isActive = row[PhotoGangBangerToPositions.semesterEnd] == null,
        )
      }

  private fun withPositions(dto: PhotoGangBangerDto): PhotoGangBangerDto = dto.copy(positions = findPositionsForMember(dto.photoGangBangerId.id))

  override fun findById(id: UUID): PhotoGangBangerDto? =
    database.photo_gang_bangers
      .find { it.id eq id }
      ?.toDto()
      ?.let { withPositions(it) }

  override fun findAll(
    page: Int,
    pageSize: Int,
  ): Page<PhotoGangBangerDto> {
    val photoGangBangers = database.photo_gang_bangers
    val dtos = photoGangBangers.toList().map { withPositions(it.toDto()) }
    return Page(page = page, pageSize = pageSize, totalRecords = photoGangBangers.totalRecords, currentList = dtos)
  }

  override fun findAllActives(
    page: Int,
    pageSize: Int,
  ): Page<PhotoGangBangerDto> {
    val photoGangBangers =
      database.photo_gang_bangers.filter {
        (it.isActive eq true).and(it.isPang eq false)
      }
    return Page(page = page, pageSize = pageSize, totalRecords = photoGangBangers.totalRecords, currentList = photoGangBangers.toList().map { withPositions(it.toDto()) })
  }

  override fun findAllActivePangs(
    page: Int,
    pageSize: Int,
  ): Page<PhotoGangBangerDto> {
    val photoGangBangers =
      database.photo_gang_bangers.filter {
        (it.isActive eq true).and(it.isPang eq true)
      }
    return Page(page = page, pageSize = pageSize, totalRecords = photoGangBangers.totalRecords, currentList = photoGangBangers.toList().map { withPositions(it.toDto()) })
  }

  override fun findAllInactivePangs(
    page: Int,
    pageSize: Int,
  ): Page<PhotoGangBangerDto> {
    val photoGangBangers =
      database.photo_gang_bangers.filter {
        (it.isActive eq false).and(it.isPang eq true)
      }
    return Page(page = page, pageSize = pageSize, totalRecords = photoGangBangers.totalRecords, currentList = photoGangBangers.toList().map { withPositions(it.toDto()) })
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

  fun findByUsername(username: String): PhotoGangBangerDto? =
    database.photo_gang_bangers
      .find { it.username eq username }
      ?.toDto()
      ?.let { withPositions(it) }
}

package no.fg.hilflingbackend.repository

import jakarta.persistence.EntityNotFoundException
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.isNull
import me.liuwj.ktorm.dsl.update
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.drop
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.sortedByDescending
import me.liuwj.ktorm.entity.take
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.PhotoGangBangerDto
import no.fg.hilflingbackend.dto.UserUploadDto
import no.fg.hilflingbackend.model.UserUploads
import no.fg.hilflingbackend.model.photo_gang_bangers
import no.fg.hilflingbackend.model.toDto
import no.fg.hilflingbackend.model.user_uploads
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.UUID

@Repository
open class UserUploadRepository(
  val database: Database,
) {
  fun findById(id: UUID): UserUploadDto? =
    database.user_uploads
      .find { (it.id eq id) and it.dateDeleted.isNull() }
      ?.let { it.toDto(ownerOf(it.photoGangBangerId)) }

  fun findAll(
    page: Int,
    pageSize: Int,
  ): Page<UserUploadDto> {
    val uploads = database.user_uploads.filter { it.dateDeleted.isNull() }
    val currentList =
      uploads
        .sortedByDescending { it.dateCreated }
        .drop(page * pageSize)
        .take(pageSize)
        .toList()
        .map { it.toDto(ownerOf(it.photoGangBangerId)) }
    return Page(
      page = page,
      pageSize = pageSize,
      totalRecords = uploads.totalRecords,
      currentList = currentList,
    )
  }

  fun findByPhotoGangBangerId(
    photoGangBangerId: UUID,
    page: Int,
    pageSize: Int,
  ): Page<UserUploadDto> {
    val uploads =
      database.user_uploads.filter {
        (it.photoGangBangerId eq photoGangBangerId) and it.dateDeleted.isNull()
      }
    val currentList =
      uploads
        .sortedByDescending { it.dateCreated }
        .drop(page * pageSize)
        .take(pageSize)
        .toList()
        .map { it.toDto(ownerOf(it.photoGangBangerId)) }
    return Page(
      page = page,
      pageSize = pageSize,
      totalRecords = uploads.totalRecords,
      currentList = currentList,
    )
  }

  fun create(dto: UserUploadDto): UserUploadDto {
    database.user_uploads.add(dto.toEntity())
    return dto
  }

  fun delete(id: UUID): Int =
    database.update(UserUploads) {
      set(it.dateDeleted, LocalDate.now())
      where { it.id eq id }
    }

  private fun ownerOf(photoGangBangerId: UUID): PhotoGangBangerDto =
    database.photo_gang_bangers
      .find { it.id eq photoGangBangerId }
      ?.toDto()
      ?: throw EntityNotFoundException("PhotoGangBanger $photoGangBangerId not found")
}

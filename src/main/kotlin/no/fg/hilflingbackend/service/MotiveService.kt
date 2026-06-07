package no.fg.hilflingbackend.service

import jakarta.persistence.EntityNotFoundException
import no.fg.hilflingbackend.dto.EventOwnerName
import no.fg.hilflingbackend.dto.MotiveCreateRequestDto
import no.fg.hilflingbackend.dto.MotiveDefaultsDto
import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.dto.MotivePatchRequestDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.SecurityLevelDto
import no.fg.hilflingbackend.repository.AlbumRepository
import no.fg.hilflingbackend.repository.EventOwnerRepository
import no.fg.hilflingbackend.repository.MotiveRepository
import no.fg.hilflingbackend.repository.PhotoRepository
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Service
class MotiveService(
  val albumRepository: AlbumRepository,
  val eventOwnerRepository: EventOwnerRepository,
  val motiveRepository: MotiveRepository,
  val photoRepository: PhotoRepository,
) {
  fun findById(
    id: UUID,
    userSecurityLevel: SecurityLevelType,
  ): MotiveDto {
    val motive =
      motiveRepository.findById(id)
        ?: throw EntityNotFoundException("Motive $id not found")
    if (motive.securityLevel.securityLevelType.ordinal < userSecurityLevel.ordinal) {
      throw AccessDeniedException("Insufficient security level to access motive $id")
    }
    return motive
  }

  fun findAll(
    page: Int,
    pageSize: Int,
    userSecurityLevel: SecurityLevelType,
  ): Page<MotiveDto> {
    val result = motiveRepository.findAll(page, pageSize)
    val filtered =
      result.currentList.filter {
        it.securityLevel.securityLevelType.ordinal >= userSecurityLevel.ordinal
      }
    return result.copy(currentList = filtered)
  }

  fun search(
    searchTerm: String,
    page: Int,
    pageSize: Int,
    userSecurityLevel: SecurityLevelType,
  ): Page<MotiveDto> {
    val result = motiveRepository.search(searchTerm, page, pageSize)
    val filtered =
      result.currentList.filter {
        it.securityLevel.securityLevelType.ordinal >= userSecurityLevel.ordinal
      }
    return result.copy(currentList = filtered)
  }

  fun create(
    dto: MotiveCreateRequestDto,
    userSecurityLevel: SecurityLevelType,
  ): MotiveDto {
    if (userSecurityLevel != SecurityLevelType.FG) {
      throw AccessDeniedException("Only FG users can create motives")
    }
    return motiveRepository.create(dto)
  }

  fun patch(
    dto: MotivePatchRequestDto,
    userSecurityLevel: SecurityLevelType,
  ): MotiveDto {
    if (userSecurityLevel != SecurityLevelType.FG) {
      throw AccessDeniedException("Only FG users can update motives")
    }
    return motiveRepository.patch(dto)
  }

  fun deleteMotive(
    id: UUID,
    userSecurityLevel: SecurityLevelType,
  ): Int {
    if (userSecurityLevel != SecurityLevelType.FG) {
      throw AccessDeniedException("Only FG users can delete motives")
    }
    if (photoRepository.hasNonDeletedPhotos(id)) {
      throw IllegalArgumentException("Cannot delete motive: all photos must be deleted first")
    }
    return motiveRepository.delete(id)
  }

  fun getMotiveDefaults(): MotiveDefaultsDto {
    val samfundet =
      eventOwnerRepository.findByEventOwnerName(EventOwnerName.Samfundet)
        ?: error("EventOwner 'Samfundet' not found in database")
    val digitalAlbum = albumRepository.findAllDigital(0, 1).currentList.firstOrNull()
    val analogAlbum = albumRepository.findAllAnalog(0, 1).currentList.firstOrNull()

    return MotiveDefaultsDto(
      eventOwnerDto = samfundet,
      securityLevel = SecurityLevelDto(SecurityLevelType.ALLE),
      albumDto = digitalAlbum,
      analogAlbumDto = analogAlbum,
      dateCreated = LocalDateTime.now(),
      date = LocalDate.now(),
    )
  }
}

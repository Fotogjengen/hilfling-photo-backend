package no.fg.hilflingbackend.service

import jakarta.persistence.EntityNotFoundException
import no.fg.hilflingbackend.dto.MediaDto
import no.fg.hilflingbackend.dto.MediaPatchRequestDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.repository.MediaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class MediaService(
  val repository: MediaRepository,
) {
  fun findById(id: UUID): MediaDto = repository.findOrNull(id) ?: throw EntityNotFoundException("Media $id not found")

  fun findAll(
    page: Int,
    pageSize: Int,
  ): Page<MediaDto> = repository.findAll(page, pageSize)

  fun create(dto: MediaDto): Int = repository.create(dto)

  // Soft delete only retires the media from selection; existing photos keep it
  fun delete(id: UUID): Int = repository.delete(id)

  @Transactional
  fun patch(dto: MediaPatchRequestDto): MediaDto {
    val current = findById(dto.mediaId.id)
    if (dto.analog != null && dto.analog != current.analog && repository.isReferenced(current.mediaId.id)) {
      throw IllegalArgumentException("Cannot change analog on media '${current.medium}': it is used by photos")
    }
    return repository.patch(dto)
  }
}

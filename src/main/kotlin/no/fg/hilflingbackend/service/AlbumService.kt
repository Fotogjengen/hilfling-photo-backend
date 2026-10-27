package no.fg.hilflingbackend.service

import jakarta.persistence.EntityNotFoundException
import no.fg.hilflingbackend.dto.AlbumDto
import no.fg.hilflingbackend.dto.AlbumId
import no.fg.hilflingbackend.dto.AlbumPatchRequestDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.repository.AlbumRepository
import no.fg.hilflingbackend.repository.PhotoRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AlbumService(
  val repository: AlbumRepository,
  val photoRepository: PhotoRepository,
) {
  fun findById(id: UUID): AlbumDto = repository.findById(id) ?: throw EntityNotFoundException("Album $id not found")

  fun findAll(
    page: Int,
    pageSize: Int,
  ): Page<AlbumDto> = repository.findAll(page, pageSize)

  fun create(dto: AlbumDto): Int = repository.create(dto)

  fun delete(id: UUID): Int = repository.delete(id)

  fun patch(dto: AlbumPatchRequestDto): AlbumDto {
    val current = findById(dto.albumId.id)
    val changesLocation = dto.name != current.name || (dto.analog != null && dto.analog != current.analog)
    // The album name is part of every photo's path, so renaming would require moving all its files
    if (changesLocation && photoRepository.hasPhotosInAlbum(current.albumId.id)) {
      throw IllegalArgumentException("Cannot rename or change the type of album '${current.name}': it contains photos")
    }
    return repository.patch(dto)
  }

  fun findAllAnalog(
    page: Int,
    pageSize: Int,
  ): Page<AlbumDto> = repository.findAllAnalog(page, pageSize)

  fun findAllDigital(
    page: Int,
    pageSize: Int,
  ): Page<AlbumDto> = repository.findAllDigital(page, pageSize)

  fun setDefault(albumId: UUID): AlbumDto =
    repository.setDefault(albumId)
        ?: throw EntityNotFoundException("Album not found")

  fun findDefault(): AlbumDto =
    repository.findDefault()
        ?: throw EntityNotFoundException("Could not find default album")
}

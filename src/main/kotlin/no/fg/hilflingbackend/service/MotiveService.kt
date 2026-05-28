package no.fg.hilflingbackend.service

import no.fg.hilflingbackend.dto.EventOwnerName
import no.fg.hilflingbackend.dto.MotiveDefaultsDto
import no.fg.hilflingbackend.dto.SecurityLevelDto
import no.fg.hilflingbackend.repository.AlbumRepository
import no.fg.hilflingbackend.repository.EventOwnerRepository
import no.fg.hilflingbackend.repository.MotiveRepository
import no.fg.hilflingbackend.repository.PhotoRepository
import no.fg.hilflingbackend.valueobject.SecurityLevelType
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
  fun deleteMotive(id: UUID): Int {
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

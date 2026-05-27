package no.fg.hilflingbackend.service

import no.fg.hilflingbackend.dto.EventOwnerName
import no.fg.hilflingbackend.dto.MotiveDefaultsDto
import no.fg.hilflingbackend.dto.SecurityLevelDto
import no.fg.hilflingbackend.repository.AlbumRepository
import no.fg.hilflingbackend.repository.EventOwnerRepository
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class MotiveService(
  val albumRepository: AlbumRepository,
  val eventOwnerRepository: EventOwnerRepository,
) {
  fun getMotiveDefaults(): MotiveDefaultsDto {
    val samfundet = eventOwnerRepository.findByEventOwnerName(EventOwnerName.Samfundet)
      ?: error("EventOwner 'Samfundet' not found in database")
    val digitalAlbum = albumRepository.findAllDigital(0, 1).currentList.firstOrNull()
    val analogAlbum = albumRepository.findAllAnalog(0, 1).currentList.firstOrNull()

    return MotiveDefaultsDto(
      eventOwnerDto = samfundet,
      securityLevel = SecurityLevelDto(SecurityLevelType.ALLE),
      albumDto = digitalAlbum,
      analogAlbumDto = analogAlbum,
      dateCreated = LocalDate.now(),
      date = LocalDate.now(),
    )
  }
}

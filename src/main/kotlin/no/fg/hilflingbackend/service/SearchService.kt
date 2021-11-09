package no.fg.hilflingbackend.service

import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.repository.SearchRepository
import org.springframework.stereotype.Service

@Service
class SearchService(val repository: SearchRepository) {
  fun findMotives(searchTerm: String): List<MotiveDto> {
    return repository.findBySearchTerm(searchTerm)
  }
}

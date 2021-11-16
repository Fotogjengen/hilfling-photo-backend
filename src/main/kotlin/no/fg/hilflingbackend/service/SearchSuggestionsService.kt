package no.fg.hilflingbackend.service

import no.fg.hilflingbackend.repository.SearchSuggestionsRepository
import org.springframework.stereotype.Service

@Service
class SearchSuggestionsService(val repository: SearchSuggestionsRepository) {
  fun findSuggestions(term: String): List<String> {
    return repository.findBySearchTerm(term)
  }
}

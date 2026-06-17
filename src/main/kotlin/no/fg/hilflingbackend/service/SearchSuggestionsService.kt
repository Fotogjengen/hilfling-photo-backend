package no.fg.hilflingbackend.service

import no.fg.hilflingbackend.dto.ActiveFilters
import no.fg.hilflingbackend.dto.FilterSuggestionsResponseDto
import no.fg.hilflingbackend.repository.SearchSuggestionsRepository
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import org.springframework.stereotype.Service

@Service
class SearchSuggestionsService(
  val repository: SearchSuggestionsRepository,
) {
  fun findSuggestions(
    term: String,
    userLevel: SecurityLevelType,
    filters: ActiveFilters,
  ): FilterSuggestionsResponseDto = repository.findFilterSuggestions(term, userLevel, filters)
}

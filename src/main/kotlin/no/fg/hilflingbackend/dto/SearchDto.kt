package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.Search

data class SearchDto(
  val suggestions: List<String>,
)

fun SearchDto.toEntity(): Search {
  val dto = this
  return Search {
    suggestions = dto.suggestions
  }
}

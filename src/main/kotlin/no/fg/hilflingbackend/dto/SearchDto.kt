package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.Search

data class SearchDto(
  var suggestions: List<String>,
)

fun SearchDto.toEntity(): Search {
  val dto = this
  return Search {
    suggestions = dto.suggestions
  }
}

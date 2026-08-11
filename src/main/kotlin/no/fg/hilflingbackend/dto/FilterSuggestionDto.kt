package no.fg.hilflingbackend.dto

import com.fasterxml.jackson.annotation.JsonProperty

enum class FilterSuggestionType {
  @JsonProperty("place")
  PLACE,

  @JsonProperty("event_owner")
  EVENT_OWNER,

  @JsonProperty("category")
  CATEGORY,

  @JsonProperty("security_level")
  SECURITY_LEVEL,

  @JsonProperty("album")
  ALBUM,

  @JsonProperty("motive")
  MOTIVE,
}

data class FilterSuggestionDto(
  val type: FilterSuggestionType,
  val id: String,
  val displayText: String,
)

data class FilterSuggestionsResponseDto(
  val suggestions: List<FilterSuggestionDto>,
  val hiddenMotiveCount: Int,
)

package no.fg.hilflingbackend.dto

enum class FilterSuggestionType {
  PLACE,
  EVENT_OWNER,
  CATEGORY,
  SECURITY_LEVEL,
  ALBUM,
  MOTIVE,
}

data class FilterSuggestionDto(
  val type: FilterSuggestionType,
  val id: String,
  val displayText: String,
)

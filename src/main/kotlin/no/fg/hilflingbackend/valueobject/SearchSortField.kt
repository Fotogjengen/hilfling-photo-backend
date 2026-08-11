package no.fg.hilflingbackend.valueobject

import com.fasterxml.jackson.annotation.JsonProperty

enum class SearchSortField {
  RELEVANCE,
  DATE_TAKEN,
  DATE_UPLOADED,
  MOTIVE_TITLE,
  CATEGORY,
  PLACE,
}

package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.FilterSuggestionDto
import no.fg.hilflingbackend.service.SearchSuggestionsService
import no.fg.hilflingbackend.utils.ResponseOk
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/search/suggestions")
class SearchSuggestionsController(
  val service: SearchSuggestionsService,
) {
  @GetMapping
  fun getSearchSuggestions(
    @RequestParam(required = false, defaultValue = "") q: String,
  ): ResponseEntity<List<FilterSuggestionDto>> = ResponseOk(service.findSuggestions(q))
}

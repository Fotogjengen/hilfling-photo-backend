package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.service.SearchSuggestionsService
import no.fg.hilflingbackend.utils.ResponseOk
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/searchSuggestions")
class SearchSuggestionsController(val service: SearchSuggestionsService) {

  @GetMapping("/{searchTerm}")
  fun getBySearchTerm(@PathVariable("searchTerm") term: String): ResponseEntity<List<String>> =
    ResponseOk(service.findSuggestions(term))
}

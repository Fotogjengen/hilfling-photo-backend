package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.repository.SearchRepository
import no.fg.hilflingbackend.utils.ResponseOk
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/search")
class SearchController(val repository: SearchRepository) {

  @GetMapping("/{searchTerm}")
  fun getBySearchTerm(@PathVariable("searchTerm") searchTerm: String): ResponseEntity<List<MotiveDto>> =
    ResponseOk(repository.findBySearchTerm(searchTerm))
}

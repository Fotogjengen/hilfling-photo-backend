package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.model.Motive
import no.fg.hilflingbackend.repository.SearchRepository
import no.fg.hilflingbackend.utils.ResponseOk
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/search")
class SearchController {
  @Autowired
  lateinit var repository: SearchRepository

  @GetMapping("/{searchTerm}")
  fun getBySearchTerm(@PathVariable("searchTerm") searchTerm: String): ResponseEntity<List<MotiveDto>> =
    ResponseOk(repository.findBySearchterm(searchTerm))

}

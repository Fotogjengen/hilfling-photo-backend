package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.repository.MotiveRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/search")
class SearchController {
  @Autowired
  lateinit var repository: MotiveRepository

  @GetMapping("/{searchTerm}")
  fun getBySearchTerm(@PathVariable("searchTerm") searchTerm: String): SearchDto? {
    return repository.find(searchTerm)
  }
}

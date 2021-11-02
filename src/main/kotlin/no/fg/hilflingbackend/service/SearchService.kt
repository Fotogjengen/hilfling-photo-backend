package no.fg.hilflingbackend.service

import no.fg.hilflingbackend.model.Motive
import no.fg.hilflingbackend.repository.SearchRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SearchService {

  lateinit var repository: SearchRepository

  fun findMotives(searchTerm: String): List<Motive> =
    repository.findBySearchterm(searchTerm)
}

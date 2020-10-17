package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.model.PhotographyRequest
import no.fg.hilflingbackend.repository.PhotographyRequestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/photography_requests")
class PhotographyRequestController {
  @Autowired
  lateinit var repository: PhotographyRequestRepository

  @GetMapping("/{id}")
  fun getById(@PathVariable("id") id: UUID): PhotographyRequest? {
    return repository.findById(id)
  }

  @GetMapping
  fun getAll(): List<PhotographyRequest> {
    return repository.findAll()
  }

  @PostMapping
  fun create(
    @RequestBody photographyRequest: PhotographyRequest
  ): PhotographyRequest {
    return repository.create(photographyRequest)
  }
}

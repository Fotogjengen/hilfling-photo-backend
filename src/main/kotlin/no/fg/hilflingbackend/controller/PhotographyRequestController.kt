package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.configurations.RequireSecurityLevel
import no.fg.hilflingbackend.model.PhotographyRequest
import no.fg.hilflingbackend.repository.PhotographyRequestRepository
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/photography_requests")
class PhotographyRequestController {
  @Autowired
  lateinit var repository: PhotographyRequestRepository

  @GetMapping("/{id}")
  @RequireSecurityLevel(SecurityLevelType.FG)
  fun getById(
    @PathVariable("id") id: UUID,
  ): PhotographyRequest? = repository.findById(id)

  @GetMapping
  @RequireSecurityLevel(SecurityLevelType.FG)
  fun getAll(): List<PhotographyRequest> = repository.findAll()

  @PostMapping
  @RequireSecurityLevel(SecurityLevelType.FG)
  fun create(
    @RequestBody photographyRequest: PhotographyRequest,
  ): PhotographyRequest = repository.create(photographyRequest)
}

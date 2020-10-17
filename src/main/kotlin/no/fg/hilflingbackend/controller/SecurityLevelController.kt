package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.model.SecurityLevel
import no.fg.hilflingbackend.repository.SecurityLevelRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/security_levels")
class SecurityLevelController {
  @Autowired
  lateinit var repository: SecurityLevelRepository

  @GetMapping("/{id}")
  fun getSecurityLevelById(@PathVariable("id") id: UUID): SecurityLevel? {
    return repository.findById(id)
  }

  @GetMapping
  fun getAllSecurityLevels(): List<SecurityLevel> {
    return repository.findAll()
  }

  @PostMapping
  fun createSecurityLevel(
    @RequestBody securityLevel: SecurityLevel
  ): SecurityLevel {
    return repository.create(securityLevel)
  }
}

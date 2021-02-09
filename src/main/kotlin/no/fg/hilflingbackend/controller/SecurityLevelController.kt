package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.model.SecurityLevel
import no.fg.hilflingbackend.repository.SecurityLevelRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

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
  ): Int {
    return repository.create(securityLevel)
  }
}

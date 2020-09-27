package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.model.SecurityLevel
import no.fg.hilflingbackend.repository.SecurityLevelRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/security_levels")
class SecurityLevelController {
    val repository = SecurityLevelRepository()

    @GetMapping("/{id}")
    fun getSecurityLevelById(@PathVariable("id") id: Int): SecurityLevel? {
        return repository.findById(id)
    }

    @GetMapping
    fun getAllSecurityLevels(): List<SecurityLevel> {
        return repository.findAll()
    }

    @PostMapping
    fun createSecurityLevel(
            @RequestParam("securityLevel") securityLevel: SecurityLevel
    ): SecurityLevel {
        return repository.create(securityLevel)
    }
}
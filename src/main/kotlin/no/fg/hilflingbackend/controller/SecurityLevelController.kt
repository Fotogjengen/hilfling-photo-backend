package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.SecurityLevelDto
import no.fg.hilflingbackend.model.SecurityLevel
import no.fg.hilflingbackend.repository.SecurityLevelRepository
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/security_levels")
open class SecurityLevelController(override val repository: SecurityLevelRepository) : BaseController<SecurityLevel, SecurityLevelDto>(repository)

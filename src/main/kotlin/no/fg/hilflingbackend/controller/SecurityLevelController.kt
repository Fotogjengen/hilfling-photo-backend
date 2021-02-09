package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.SecurityLevelDto
import no.fg.hilflingbackend.model.PurchaseOrder
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
open class SecurityLevelController(override val repository: SecurityLevelRepository) : BaseController<SecurityLevel, SecurityLevelDto>(repository)

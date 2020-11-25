package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.GangDto
import no.fg.hilflingbackend.model.Gang
import no.fg.hilflingbackend.repository.GangRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/gangs")
open class GangController(override val repository: GangRepository): BaseController<Gang, GangDto>(repository) {

}

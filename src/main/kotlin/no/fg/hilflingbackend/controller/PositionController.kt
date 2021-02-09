package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.PositionDto
import no.fg.hilflingbackend.model.Position
import no.fg.hilflingbackend.repository.PositionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/positions")
open class PositionController(override val repository: PositionRepository) : BaseController<Position, PositionDto>(repository)

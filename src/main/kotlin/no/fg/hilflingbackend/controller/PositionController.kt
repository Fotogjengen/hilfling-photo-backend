package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.PositionDto
import no.fg.hilflingbackend.dto.PositionPatchRequestDto
import no.fg.hilflingbackend.model.Position
import no.fg.hilflingbackend.repository.PositionRepository
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/positions")
open class PositionController(override val repository: PositionRepository) : BaseController<Position, PositionDto, PositionPatchRequestDto>(repository)

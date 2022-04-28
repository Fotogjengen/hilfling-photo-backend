package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.GangDto
import no.fg.hilflingbackend.dto.GangPatchRequestDto
import no.fg.hilflingbackend.model.Gang
import no.fg.hilflingbackend.repository.GangRepository
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/gangs")
open class GangController(override val repository: GangRepository) : BaseController<Gang, GangDto, GangPatchRequestDto>(repository)

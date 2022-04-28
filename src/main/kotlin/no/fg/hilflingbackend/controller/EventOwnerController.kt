package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.EventOwnerDto
import no.fg.hilflingbackend.model.EventOwner
import no.fg.hilflingbackend.repository.EventOwnerRepository
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/event_owners")
open class EventOwnerController(override val repository: EventOwnerRepository) : BaseController<EventOwner, EventOwnerDto, NotImplementedError>(repository)

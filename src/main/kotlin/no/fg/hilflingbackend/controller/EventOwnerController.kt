package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.EventOwnerDto
import no.fg.hilflingbackend.model.EventOwner
import no.fg.hilflingbackend.repository.EventOwnerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/event_owners")
open class EventOwnerController(override val repository: EventOwnerRepository) : BaseController<EventOwner, EventOwnerDto>(repository)

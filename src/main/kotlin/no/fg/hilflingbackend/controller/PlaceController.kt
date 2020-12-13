package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.PlaceDto
import no.fg.hilflingbackend.model.Place
import no.fg.hilflingbackend.repository.PlaceRepository
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/places")
open class PlaceController(override val repository: PlaceRepository) : BaseController<Place, PlaceDto>(repository)

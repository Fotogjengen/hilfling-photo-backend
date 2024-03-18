package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.PlaceDto
import no.fg.hilflingbackend.dto.PlacePatchRequestDto
import no.fg.hilflingbackend.model.Place
import no.fg.hilflingbackend.repository.PlaceRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/places")
open class PlaceController(override val repository: PlaceRepository) : BaseController<Place, PlaceDto, PlacePatchRequestDto>(repository) {
@GetMapping("/getUuidByTitle")
fun getUuidByPlace(
    @RequestParam(
        "placeTitle",
    ) placeTitle: String,
    ): UUID? {
        return repository.findUuidByPlace(placeTitle)
    }
}

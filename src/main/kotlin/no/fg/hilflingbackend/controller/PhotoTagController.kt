package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.PhotoTagDto
import no.fg.hilflingbackend.model.PhotoTag
import no.fg.hilflingbackend.repository.PhotoTagRepository
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping("/phototags")
open class PhotoTagController(override val repository: PhotoTagRepository) : BaseController<PhotoTag, PhotoTagDto>(repository)

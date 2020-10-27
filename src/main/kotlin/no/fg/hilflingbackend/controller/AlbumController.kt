package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.model.Album
import no.fg.hilflingbackend.repository.AlbumRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/albums")
class AlbumController(override val repository: AlbumRepository): BaseController<Album>(repository) {
}

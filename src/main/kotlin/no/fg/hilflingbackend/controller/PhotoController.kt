package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.model.AnalogPhoto
import no.fg.hilflingbackend.model.Photo
import no.fg.hilflingbackend.model.SecurityLevel
import no.fg.hilflingbackend.repository.PhotoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/photos")
class PhotoController {
    @Autowired
    lateinit var repository: PhotoRepository

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Int): Photo? {
        return repository.findById(id)
    }

    @GetMapping
    fun getAll(
            securityLevel: SecurityLevel?
    ): List<Photo> {
        securityLevel ?: return repository.findAll()
        return repository.findBySecurityLevel(securityLevel)
    }

    @GetMapping("/analog")
    fun getAllAnalogPhotos(): List<Photo> {
        return repository.findAllAnalogPhotos()
    }

    @GetMapping("/digital")
    fun getAllDigitalPhotos(): List<Photo> {
        return repository.findAllDigitalPhotos()
    }

    @PostMapping
    fun createPhoto(
            @RequestParam("photo") photo: Photo
    ): Photo {
        return repository.createPhoto(photo)
    }

    @PostMapping("/analog")
    fun createPhoto(
            @RequestParam("photo") analogPhoto: AnalogPhoto
    ): AnalogPhoto {
        return repository.createAnalogPhoto(analogPhoto)
    }
}
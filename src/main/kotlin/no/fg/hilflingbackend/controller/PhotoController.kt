package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.model.AnalogPhoto
import no.fg.hilflingbackend.model.Photo
import no.fg.hilflingbackend.model.SecurityLevel
import no.fg.hilflingbackend.repository.PhotoRepository
import no.fg.hilflingbackend.service.PhotoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/photos")
class PhotoController {

    @Autowired
    lateinit var photoService: PhotoService

    @Autowired
    lateinit var repository: PhotoRepository

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Int): Photo? {
        return repository.findById(id)
    }

    @GetMapping
    fun getAll(): List<Photo> {
        return repository.findAll()
    }

    @GetMapping("/analog")
    fun getAllAnalogPhotos(): List<Photo> {
        return repository.findAllAnalogPhotos()
    }

    @GetMapping("/digital")
    fun getAllDigitalPhotos(): List<Photo> {
        return repository.findAllDigitalPhotos()
    }

    /*@PostMapping
    fun createPhoto(
            @RequestParam("photo") photo: Photo,
            @RequestParam("file") file: MultipartFile
    ): Photo {

        return repository.createPhoto(photo)
    }*/

    @PostMapping
    fun createPhoto(
            @RequestPart("photo") photo: Photo,
            @RequestPart("file") file: MultipartFile
    ): Photo {
        val path = photoService.store(file, photo.securityLevel)
        // TODO: test photo upload
        photo.smallUrl = path
        photo.mediumUrl = path
        photo.largeUrl = path
        repository.createPhoto(photo)
        return photo
    }

    @PostMapping("/analog")
    fun createPhoto(
            @RequestBody analogPhoto: AnalogPhoto
    ): AnalogPhoto {
        return repository.createAnalogPhoto(analogPhoto)
    }
}
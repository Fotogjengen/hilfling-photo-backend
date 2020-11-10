package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.model.AnalogPhoto
import no.fg.hilflingbackend.model.Photo
import no.fg.hilflingbackend.repository.PhotoRepository
import no.fg.hilflingbackend.repository.SecurityLevelRepository
import no.fg.hilflingbackend.service.PhotoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("/photos")
class PhotoController {

  @Autowired
  lateinit var photoService: PhotoService

  @Autowired
  lateinit var repository: PhotoRepository

  @Autowired
  lateinit var securityLevelRepository: SecurityLevelRepository

  @GetMapping("/{id}")
  fun getById(@PathVariable("id") id: UUID): Photo? {
    return repository.findById(id)
  }


  @GetMapping
  fun getAll(): List<Photo> {
    return repository.findAll()
  }

  @GetMapping("/carousel")
  fun getCarouselPhotos(): List<Photo> {
    return repository.findCarouselPhotos()
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
  @PostMapping("/profile")
  private fun uploadPhotoFile(
    @RequestPart("file") file: MultipartFile,
    //@RequestPart("type") type: String,
  ): String {
    return photoService.store(file, "PROFILE")
  }

  @PostMapping
  fun uploadPhoto(
    @RequestPart("photo") photo: Photo,
    @RequestPart("file") file: MultipartFile
  ): ResponseEntity<Photo> {
    val path = photoService.store(file, photo.securityLevel.type)
    photo.smallUrl = path
    photo.mediumUrl = path
    photo.largeUrl = path
    val createdPhoto = repository.createPhoto(photo)
    return ResponseEntity<Photo>(createdPhoto, HttpHeaders(), HttpStatus.CREATED)
  }

  @PostMapping("/analog")
  fun createAnalogPhoto(
    @RequestBody analogPhoto: AnalogPhoto
  ): AnalogPhoto {
    return repository.createAnalogPhoto(analogPhoto)
  }

  @PatchMapping("/analog")
  fun uploadAnalogPhoto(
    @RequestPart("photo") analogPhoto: AnalogPhoto,
    @RequestPart("file") file: MultipartFile
  ): ResponseEntity<AnalogPhoto> {
    val path = photoService.store(file, analogPhoto.photo.securityLevel.type)
    analogPhoto.photo.smallUrl = path
    analogPhoto.photo.mediumUrl = path
    analogPhoto.photo.largeUrl = path
    val updatedAnalogPhoto = repository.patchAnalogPhoto(analogPhoto)
    return ResponseEntity<AnalogPhoto>(updatedAnalogPhoto, HttpHeaders(), HttpStatus.ACCEPTED)
  }
}

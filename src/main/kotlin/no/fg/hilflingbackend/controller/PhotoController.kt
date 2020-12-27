package no.fg.hilflingbackend.controller

import kotlinx.coroutines.runBlocking
import no.fg.hilflingbackend.dto.GangDto
import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.exceptions.GlobalExceptionHandler
import no.fg.hilflingbackend.model.AnalogPhoto
import no.fg.hilflingbackend.model.SecurityLevel
import no.fg.hilflingbackend.repository.GangRepository
import no.fg.hilflingbackend.repository.MotiveRepository
import no.fg.hilflingbackend.repository.PhotoGangBangerRepository
import no.fg.hilflingbackend.repository.PhotoRepository
import no.fg.hilflingbackend.repository.PlaceRepository
import no.fg.hilflingbackend.repository.SecurityLevelRepository
import no.fg.hilflingbackend.service.PhotoService
import no.fg.hilflingbackend.utils.memoize
import no.fg.hilflingbackend.value_object.ImageFileName
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.security.InvalidParameterException
import java.util.UUID
import javax.persistence.EntityNotFoundException

@RestController
@RequestMapping("/photos")
class PhotoController(
  val gangRepository: GangRepository,
  val photoRepository: PhotoRepository,
  val motiveRepository: MotiveRepository,
  val placeRepository: PlaceRepository,
  val securityLevelRepository: SecurityLevelRepository,
  val photoGangBangerRepository: PhotoGangBangerRepository,
  val photoService: PhotoService

) : GlobalExceptionHandler() {
  // TODO: Remove not used anyMOre
  private fun uploadPhotoFile(
    file: MultipartFile,
    securityLevel: SecurityLevel
  ): String {
    return photoService.store(file, securityLevel)
  }

  @PostMapping
  suspend fun uploadPhoto(
    @RequestParam("isGoodPictureList") isGoodPictureList: List<Boolean>,
    @RequestParam("motiveIdList") motiveIdList: List<UUID>,
    @RequestParam("placeIdList") placeIdList: List<UUID>,
    @RequestParam("securityLevelIdList") securityLevelIdList: List<UUID>,
    @RequestParam("gangIdList") gangIdList: List<UUID>,
    @RequestParam("photoGangBangerIdList") photoGangBangerIdList: List<UUID>,
    @RequestParam("fileList") fileList: List<MultipartFile>,
  ): ResponseEntity<List<String>> {
    // Assert all fields are populated
    if (
      !(
        isGoodPictureList.size == motiveIdList.size &&
          placeIdList.size == securityLevelIdList.size &&
          gangIdList.size == photoGangBangerIdList.size &&
          isGoodPictureList.size == fileList.size
        )
    ) {
      logger.error("Parameter lists are unequal")
      throw InvalidParameterException("Parameter lists are unequal")
    }

    val cachedMotive = { id: UUID ->
      motiveRepository
        .findById(id) ?: throw EntityNotFoundException("Did not find motive")
    }.memoize()

    // TODO: Test speed
    val cachedPlace = { id: UUID ->
      runBlocking {
        placeRepository
          .findById(id) ?: throw EntityNotFoundException("Did not find place")
      }
    }.memoize()

    val createdPhotoList = fileList.mapIndexed { index, file ->
      logger.info("Request with parameters: ${fileList.get(index).originalFilename}  ${isGoodPictureList.get(index)}, ${motiveIdList.get(index)}")

      val place = placeRepository
        .findById(placeIdList.get(index)) ?: throw EntityNotFoundException("Did not find place")

      // TODO: Use DTO
      val securityLevel: SecurityLevel = securityLevelRepository
        .findById(securityLevelIdList.get(index)) ?: throw EntityNotFoundException("Did not find securitulevel")

      val gang: GangDto = gangRepository
        .findById(gangIdList.get(index)) ?: throw EntityNotFoundException("Did not find gang")

      val photoGangBanger = photoGangBangerRepository
        .findById(photoGangBangerIdList.get(index)) ?: throw EntityNotFoundException("Did not find photoGangBanger")

      val validatedFileName = ImageFileName(file.originalFilename ?: "")

      // Generate Objects
      val (photoDto, filePath) = PhotoDto.createPhotoDtoAndGeneratePaths(
        isGoodPicture = isGoodPictureList.get(index),
        gang = gang,
        motive = cachedMotive(motiveIdList.get(index)),
        securityLevel = securityLevel,
        place = cachedPlace(placeIdList.get(index)).toEntity(),
        photoGangBangerDto = photoGangBanger,
        fileName = validatedFileName,
      )
      // Save file to disk
      try {
        logger.info("Saving file to disk $filePath")
        Files.copy(file.inputStream, filePath).toString()
      } catch (ex: IOException) {
        logger.error(filePath.toString())
        logger.error(filePath.toAbsolutePath())
        logger.error(ex)
        throw IOException("Something went wrong when saving image file to disk")
      }
      // Add PhotoModel to Database
      try {
        photoRepository
          .createPhoto(photoDto.toEntity())
        logger.info("Photo created: ${photoDto.largeUrl} ")
      } catch (ex: Exception) {
        logger.error("Failed to save Photo to Database. Deleting file")
        Files.delete(filePath)
      }
      photoDto
    }

    return ResponseEntity<List<String>>(createdPhotoList.map { it.largeUrl }, HttpHeaders(), HttpStatus.CREATED)
  }

  @PostMapping("/analog")
  suspend fun createAnalogPhoto(
    @RequestBody analogPhoto: AnalogPhoto
  ): AnalogPhoto {
    return photoRepository.createAnalogPhoto(analogPhoto)
  }

  @PatchMapping("/analog")
  suspend fun uploadAnalogPhoto(
    @RequestPart("photo") analogPhoto: AnalogPhoto,
    @RequestPart("file") file: MultipartFile
  ): ResponseEntity<AnalogPhoto> {
    // TODO: Use DTO
    val securityLevel: SecurityLevel = securityLevelRepository
      .findById(analogPhoto.photo.securityLevel.id) ?: throw EntityNotFoundException("Did not find securitulevel")
    val path = uploadPhotoFile(file, securityLevel)
    analogPhoto.photo.smallUrl = path
    analogPhoto.photo.mediumUrl = path
    analogPhoto.photo.largeUrl = path
    val updatedAnalogPhoto = photoRepository.patchAnalogPhoto(analogPhoto)
    return ResponseEntity<AnalogPhoto>(updatedAnalogPhoto, HttpHeaders(), HttpStatus.ACCEPTED)
  }

  @GetMapping("/{id}")
  suspend fun getById(@PathVariable("id") id: UUID): PhotoDto {
    return photoRepository.findById(id) ?: throw EntityNotFoundException("Dit not find photo")
  }

  @GetMapping
  suspend fun getAll(): List<PhotoDto> {
    return photoRepository.findAll()
  }

  @GetMapping("/carousel")
  suspend fun getCarouselPhotos(): List<PhotoDto> {
    return photoRepository.findCarouselPhotos()
  }

  @GetMapping("/analog")
  suspend fun getAllAnalogPhotos(): List<PhotoDto> {
    return photoRepository.findAllAnalogPhotos()
  }

  @GetMapping("/digital")
  suspend fun getAllDigitalPhotos(): List<PhotoDto> {
    return photoRepository.findAllDigitalPhotos()
  }
}

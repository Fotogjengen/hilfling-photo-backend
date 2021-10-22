package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.exceptions.GlobalExceptionHandler
import no.fg.hilflingbackend.model.AnalogPhoto
import no.fg.hilflingbackend.service.PhotoService
import no.fg.hilflingbackend.utils.ResponseOk
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
import java.security.InvalidParameterException
import java.util.UUID

@RestController
@RequestMapping("/photos")
class PhotoController(
  val photoService: PhotoService
) : GlobalExceptionHandler() {
  // TODO: Remove not used anyMOre
  @PostMapping("/profile", consumes = ["multipart/form-data"])
  private fun uploadPhotoFile(
    @RequestPart("file") file: MultipartFile,
    // @RequestPart("type") type: String,
  ): String {
    // TODO: Refactor to use photoDto as in photoService
    // return photoService.store(file, SecurityLevelType.valueOf("PROFILE"))
    return "TODO"
  }

  // The main photo-upload endpoint used most of the time
  @PostMapping("/upload")
  fun uploadPhotos(
    @RequestParam("motiveTitle") motiveTitle: String,
    @RequestParam("placeName") placeName: String,
    @RequestParam("securityLevelId") securityLevelId: UUID,
    @RequestParam("photoGangBangerId") photoGangBangerId: UUID,
    @RequestParam("albumId") albumId: UUID,
    @RequestParam("categoryName") categoryName: String,
    @RequestParam("eventOwnerName") eventOwnerName: String,
    @RequestParam("photoFileList") photoFileList: List<MultipartFile>,
    @RequestParam("isGoodPhotoList") isGoodPhotoList: List<Boolean>,
    @RequestParam("tagList")tagList: List<List<String>>
  ): ResponseEntity<List<String>> =
    ResponseEntity(
      photoService.createNewMotiveAndSaveDigitalPhotos(
        motiveTitle = motiveTitle,
        placeName = placeName,
        eventOwnerName = eventOwnerName,
        securityLevelId = securityLevelId,
        albumId = albumId,
        photoGangBangerId = photoGangBangerId,
        photoFileList = photoFileList,
        tagList = tagList,
        categoryName = categoryName,
        isGoodPhotoList = isGoodPhotoList
      ),
      HttpStatus.CREATED,
    )

  @PostMapping
  fun uploadPhoto(
    @RequestParam("isGoodPictureList") isGoodPictureList: List<Boolean>,
    @RequestParam("motiveIdList") motiveIdList: List<UUID>,
    @RequestParam("placeIdList") placeIdList: List<UUID>,
    @RequestParam("securityLevelIdList") securityLevelIdList: List<UUID>,
    @RequestParam("gangIdList") gangIdList: List<UUID>,
    @RequestParam("photoGangBangerIdList") photoGangBangerIdList: List<UUID>,
    @RequestParam("albumIdList") albumIdList: List<UUID>,
    @RequestParam("categoryIdList") categoryIdList: List<UUID>,
    @RequestParam("fileList") fileList: List<MultipartFile>
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
    /*
    // TODO: Test speed
    // This is a test cache results. Not in use at the moment
    val cachedPlace = { id: UUID ->
        placeRepository
          .findById(id)
          ?: throw EntityNotFoundException("Did not find place")
    }.memoize()
     */

    val createdPhotoList = photoService
      .saveDigitalPhotos(
        isGoodPictureList,
        motiveIdList,
        placeIdList,
        securityLevelIdList,
        gangIdList,
        photoGangBangerIdList,
        albumIdList,
        categoryIdList,
        fileList,
      )

    return ResponseEntity<List<String>>(
      createdPhotoList,
      HttpHeaders(),
      HttpStatus.CREATED
    )
  }

  @PostMapping("/analog")
  fun createAnalogPhoto(
    @RequestBody analogPhoto: AnalogPhoto
  ): AnalogPhoto {
    TODO("Implement")
  }

  @PatchMapping("/analog")
  fun uploadAnalogPhoto(
    @RequestPart("photo") analogPhoto: AnalogPhoto,
    @RequestPart("file") file: MultipartFile
  ): ResponseEntity<AnalogPhoto> {
    TODO("Implement")
  }

  @GetMapping("/{id}")
  fun getById(@PathVariable("id") id: UUID): ResponseEntity<PhotoDto> = ResponseOk(
    photoService
      .getById(id)
  )

  @GetMapping("/motive/{id}")
  fun getByMotiveId(@PathVariable("id") id: UUID): ResponseEntity<List<PhotoDto>?> = ResponseOk(
    photoService
      .getByMotiveId(id)
  )

  @GetMapping
  fun getAll(): ResponseEntity<List<PhotoDto>> = ResponseOk(
    photoService
      .getAll(),
  )

  @GetMapping("/carousel")
  fun getCarouselPhotos(): ResponseEntity<List<PhotoDto>> = ResponseOk(
    photoService
      .getCarouselPhotos()
  )

  @GetMapping("/analog")
  fun getAllAnalogPhotos(): ResponseEntity<List<PhotoDto>> = ResponseOk(
    photoService
      .getAllAnalogPhotos()
  )

  fun getAllDigitalPhotos(): ResponseEntity<List<PhotoDto>> = ResponseOk(
    photoService
      .getAllDigitalPhotos()
  )
}

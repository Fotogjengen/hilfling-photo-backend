package no.fg.hilflingbackend.controller

import me.liuwj.ktorm.database.Database
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.dto.PhotoPatchRequestDto
import no.fg.hilflingbackend.exceptions.GlobalExceptionHandler
import no.fg.hilflingbackend.service.PhotoService
import no.fg.hilflingbackend.utils.ResponseOk
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.InvalidParameterException
import java.time.LocalDate
import java.util.*

@RestController
@RequestMapping("/photos")
class PhotoController(
  val photoService: PhotoService,
  val database: Database
) : GlobalExceptionHandler() {

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
    @RequestParam("tagList")tagList: List<String>,
  ): ResponseEntity<List<String>> =
    ResponseEntity(
      photoService.createNewMotiveAndSaveDigitalPhotos(
        motiveString = motiveTitle,
        placeString = placeName,
        eventOwnerString = eventOwnerName,
        securityLevelId = securityLevelId,
        albumId = albumId,
        photoGangBangerId = photoGangBangerId,
        photoFileList = photoFileList,
        tagList = tagList,
        categoryName = categoryName,
        isGoodPhotoList = isGoodPhotoList,
        dateCreated = LocalDate.now()
      ),
      HttpStatus.CREATED
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
        fileList
      )

    return ResponseEntity<List<String>>(
      createdPhotoList,
      HttpHeaders(),
      HttpStatus.CREATED
    )
  }

  @GetMapping("/{id}")
  fun getById(@PathVariable("id") id: UUID): ResponseEntity<PhotoDto> = ResponseOk(
    photoService
      .findById(id)
  )

  @GetMapping("/motive/{id}")
  fun getByMotiveId(
    @PathVariable("id") id: UUID,
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?
  ): ResponseEntity<Page<PhotoDto>?> = ResponseOk(
    photoService
      .getByMotiveId(id, page ?: 0, pageSize ?: 100)
  )

  @GetMapping
  fun getAll(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
    @RequestParam("motive", required = false) motive: UUID?,
    @RequestParam("tag", required = false) tag: List<String>?,
    @RequestParam("fromDate", required = false) fromDate: String?,
    @RequestParam("toDate", required = false) toDate: String?,
    @RequestParam("category", required = false) category: String?,
    @RequestParam("place", required = false) place: UUID?,
    @RequestParam("isGoodPic", required = false) isGoodPic: Boolean?,
    @RequestParam("album", required = false) album: UUID?,
    @RequestParam("sortBy", required = false) sortBy: String?,
    @RequestParam("desc", required = false) desc: Boolean?,
    @RequestParam("isAnalog", required = false) isAnalog: Boolean?,
  ): ResponseEntity<Page<PhotoDto>> =
    ResponseOk(
      photoService
        .getAll(
          page ?: 0,
          pageSize ?: 100,
          motive ?: UUID(0L, 0L),
          tag ?: listOf<String>(),
          LocalDate.parse(fromDate ?: "1970-01-01") ?: LocalDate.now(),
          LocalDate.parse(toDate ?: LocalDate.now().toString()) ?: LocalDate.now(),
          category ?: "",
          place ?: UUID(0L, 0L),
          isGoodPic ?: false,
          album ?: UUID(0L, 0L),
          sortBy ?: "",
          desc ?: true
        ),
    )

  @GetMapping("/carousel")
  fun getCarouselPhotos(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?
  ): ResponseEntity<Page<PhotoDto>> = ResponseOk(
    photoService
      .getCarouselPhotos(page ?: 0, pageSize ?: 6)
  )

  @GetMapping("/analog")
  fun getAllAnalogPhotos(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?
  ): ResponseEntity<Page<PhotoDto>> = ResponseOk(
    photoService
      .getAllAnalogPhotos(page ?: 0, pageSize ?: 100)
  )

  @GetMapping("/digital")
  fun getAllDigitalPhotos(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
    @RequestParam("motive", required = false) motive: UUID?,
    @RequestParam("tag", required = false) tag: List<String>?,
    @RequestParam("fromDate", required = false) fromDate: String?,
    @RequestParam("toDate", required = false) toDate: String?,
    @RequestParam("category", required = false) category: String?,
    @RequestParam("place", required = false) place: UUID?,
    @RequestParam("isGoodPic", required = false) isGoodPic: Boolean?,
    @RequestParam("album", required = false) album: UUID?,
    @RequestParam("sortBy", required = false) sortBy: String?,
    @RequestParam("desc", required = false) desc: Boolean?,
  ): ResponseEntity<Page<PhotoDto>> {
    return ResponseOk(
      photoService
        .getAllDigitalPhotos(
          page ?: 0,
          pageSize ?: 100,
          motive ?: UUID(0L, 0L),
          tag ?: listOf<String>(),
          LocalDate.parse(fromDate) ?: LocalDate.now(),
          LocalDate.parse(toDate) ?: LocalDate.now(),
          category ?: "",
          place ?: UUID(0L, 0L),
          isGoodPic ?: false,
          album ?: UUID(0L, 0L),
          sortBy ?: "",
          desc ?: true
        )
    )
  }

  @PatchMapping
  fun patch(
    @RequestBody dto: PhotoPatchRequestDto
  ): PhotoDto {
    return photoService.patch(dto)
  }
}

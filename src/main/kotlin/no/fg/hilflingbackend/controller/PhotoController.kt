package no.fg.hilflingbackend.controller

import jakarta.servlet.http.HttpServletRequest
import me.liuwj.ktorm.database.Database
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.dto.PhotoPatchRequestDto
import no.fg.hilflingbackend.exceptions.GlobalExceptionHandler
import no.fg.hilflingbackend.service.JwtService
import no.fg.hilflingbackend.service.PhotoService
import no.fg.hilflingbackend.utils.ResponseOk
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.InvalidParameterException
import java.time.LocalDate
import java.util.UUID

@RestController
@RequestMapping("/photos")
class PhotoController(
  val photoService: PhotoService,
  val database: Database,
  val jwtService: JwtService,
) : GlobalExceptionHandler() {
  private fun allowedSecurityLevels(request: HttpServletRequest): List<String> = jwtService.allowedSecurityLevels(request.getHeader("X-hilfling-token"))

  data class PreValidatePhotoUploadResponse(
    val isValid: Boolean,
    val errors: List<String>,
  )

  @PostMapping("/upload/validate")
  fun preValidatePhotoUpload(
    @RequestParam("motiveId") motiveid: String,
    @RequestParam("placeId") placeId: String,
    @RequestParam("photoGangBangerId") photoGangBangerId: UUID,
    @RequestParam("albumId") albumTitle: String,
    @RequestParam("categoryId") categoryId: String,
    @RequestParam("eventOwnerId") eventOwnerId: String,
  ): ResponseEntity<PreValidatePhotoUploadResponse> {
    val errors =
      photoService.preValidatePhotoUpload(
        motiveId = motiveid,
        placeId = placeId,
        photoGangBangerId = photoGangBangerId,
        albumTitle = albumTitle,
        categoryId = categoryId,
        eventOwnerId = eventOwnerId,
      )

    return ResponseEntity.ok(
      PreValidatePhotoUploadResponse(
        isValid = errors.isEmpty(),
        errors = errors,
      ),
    )
  }

  // The main photo-upload endpoint used most of the time
  @PostMapping("/upload")
  fun uploadPhotos(
    @RequestParam("motiveTitle") motiveTitle: String,
    @RequestParam("placeName") placeName: String,
    @RequestParam("securityLevel") securityLevel: String,
    @RequestParam("photoGangBangerId") photoGangBangerId: UUID,
    @RequestParam("albumTitle") albumTitle: String,
    @RequestParam("categoryName") categoryName: String,
    @RequestParam("eventOwnerName") eventOwnerName: String,
    @RequestParam("smallUrl") smallUrl: String,
    @RequestParam("mediumUrl") mediumUrl: String,
    @RequestParam("largeUrl") largeUrl: String,
    @RequestParam("isGoodPhoto") isGoodPhoto: Boolean,
    @RequestParam(value = "tagList", required = false) tagList: List<String>?,
    @RequestParam("dateTaken") dateTaken: LocalDate,
  ): ResponseEntity<List<PhotoDto>> =
    ResponseEntity(
      photoService.createNewMotiveAndSaveDigitalPhotos(
        motiveString = motiveTitle,
        placeString = placeName,
        eventOwnerString = eventOwnerName,
        securityLevel = securityLevel,
        albumTitle = albumTitle,
        photoGangBangerId = photoGangBangerId,
        smallUrl = smallUrl,
        mediumUrl = mediumUrl,
        largeUrl = largeUrl,
        tagList = tagList ?: emptyList(),
        categoryName = categoryName,
        isGoodPhoto = isGoodPhoto,
        dateTaken = dateTaken,
      ),
      HttpStatus.CREATED,
    )

  @PostMapping
  fun uploadPhoto(
    @RequestParam("isGoodPictureList") isGoodPictureList: List<Boolean>,
    @RequestParam("motiveIdList") motiveIdList: List<UUID>,
    @RequestParam("placeIdList") placeIdList: List<UUID>,
    @RequestParam("securityLevelList") securityLevelList: List<String>,
    @RequestParam("gangIdList") gangIdList: List<UUID>,
    @RequestParam("photoGangBangerIdList") photoGangBangerIdList: List<UUID>,
    @RequestParam("albumIdList") albumIdList: List<UUID>,
    @RequestParam("categoryIdList") categoryIdList: List<UUID>,
    @RequestParam("fileNameList") fileNameList: List<String>,
    @RequestParam("dateTaken") dateTaken: LocalDate,
  ): ResponseEntity<List<PhotoDto>> {
    // Assert all fields are populated
    if (!(
        isGoodPictureList.size == motiveIdList.size &&
          placeIdList.size == securityLevelList.size &&
          gangIdList.size == photoGangBangerIdList.size &&
          isGoodPictureList.size == fileNameList.size
      )
    ) {
      logger.error("Parameter lists are unequal")
      throw InvalidParameterException("Parameter lists are unequal")
    }

    return ResponseEntity(
      photoService.saveDigitalPhotos(
        isGoodPictureList,
        motiveIdList,
        placeIdList,
        securityLevelList,
        gangIdList,
        photoGangBangerIdList,
        albumIdList,
        categoryIdList,
        fileNameList,
        dateTaken,
      ),
      HttpStatus.CREATED,
    )
  }

  @GetMapping("/{id}")
  fun getById(
    request: HttpServletRequest,
    @PathVariable("id") id: UUID,
  ): ResponseEntity<PhotoDto> =
    ResponseOk(
      photoService.findById(id, allowedSecurityLevels(request)),
    )

  @GetMapping("/motive/{id}")
  fun getByMotiveId(
    request: HttpServletRequest,
    @PathVariable("id") id: UUID,
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
  ): ResponseEntity<Page<PhotoDto>> =
    ResponseOk(
      photoService.getByMotiveId(id, page ?: 0, pageSize ?: 100, allowedSecurityLevels(request)),
    )

  @GetMapping
  fun getAll(
    request: HttpServletRequest,
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
      photoService.getAll(
        page ?: 0,
        pageSize ?: 100,
        motive ?: UUID(0L, 0L),
        tag ?: listOf<String>(),
        LocalDate.parse(fromDate ?: "1970-01-01") ?: LocalDate.now(),
        LocalDate.parse(toDate ?: LocalDate.now().toString())
          ?: LocalDate.now(),
        category ?: "",
        place ?: UUID(0L, 0L),
        isGoodPic ?: false,
        album ?: UUID(0L, 0L),
        sortBy ?: "",
        desc ?: true,
        allowedSecurityLevels = allowedSecurityLevels(request),
        isAnalog ?: false,
      ),
    )

  @GetMapping("/goodPhotos")
  fun getGoodPhotos(
    request: HttpServletRequest,
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
  ): ResponseEntity<Page<PhotoDto>> =
    ResponseOk(
      photoService.getGoodPhotos(page ?: 0, pageSize ?: 10, allowedSecurityLevels(request)),
    )

  @GetMapping("/analog")
  fun getAllAnalogPhotos(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
  ): ResponseEntity<Page<PhotoDto>> =
    ResponseOk(
      photoService.getAllAnalogPhotos(page ?: 0, pageSize ?: 100),
    )

  @GetMapping("/digital")
  fun getAllDigitalPhotos(
    request: HttpServletRequest,
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
  ): ResponseEntity<Page<PhotoDto>> =
    ResponseOk(
      photoService.getAllDigitalPhotos(
        page ?: 0,
        pageSize ?: 100,
        motive ?: UUID(0L, 0L),
        tag ?: listOf<String>(),
        fromDate?.let { LocalDate.parse(it) } ?: LocalDate.now(),
        toDate?.let { LocalDate.parse(it) } ?: LocalDate.now(),
        category ?: "",
        place ?: UUID(0L, 0L),
        isGoodPic ?: false,
        album ?: UUID(0L, 0L),
        sortBy ?: "",
        desc ?: true,
        allowedSecurityLevels = allowedSecurityLevels(request),
        isAnalog = false,
      ),
    )

  @PatchMapping
  fun patch(
    @RequestBody dto: PhotoPatchRequestDto,
  ): PhotoDto = photoService.patch(dto)
}

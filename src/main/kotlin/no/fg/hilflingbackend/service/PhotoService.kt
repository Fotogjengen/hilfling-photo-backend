package no.fg.hilflingbackend.service

import jakarta.persistence.EntityNotFoundException
import no.fg.hilflingbackend.dto.EventOwnerName
import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.dto.PhotoPatchRequestDto
import no.fg.hilflingbackend.dto.SecurityLevelDto
import no.fg.hilflingbackend.model.toDto
import no.fg.hilflingbackend.repository.AlbumRepository
import no.fg.hilflingbackend.repository.CategoryRepository
import no.fg.hilflingbackend.repository.EventOwnerRepository
import no.fg.hilflingbackend.repository.MotiveRepository
import no.fg.hilflingbackend.repository.PhotoGangBangerRepository
import no.fg.hilflingbackend.repository.PhotoRepository
import no.fg.hilflingbackend.repository.PhotoTagRepository
import no.fg.hilflingbackend.repository.PlaceRepository
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.UUID

@Service
class PhotoService(
  val photoRepository: PhotoRepository,
  val photoTagRepository: PhotoTagRepository,
  val motiveRepository: MotiveRepository,
  val eventOwnerRepository: EventOwnerRepository,
  val placeRepository: PlaceRepository,
  val categoryRepository: CategoryRepository,
  val albumRepository: AlbumRepository,
  val photoGangBangerRepository: PhotoGangBangerRepository,
) {
  val logger = LoggerFactory.getLogger(this::class.java)

  fun preValidatePhotoUpload(
    motiveId: String,
    placeId: String,
    photoGangBangerId: UUID,
    albumTitle: String,
    categoryId: String,
    eventOwnerId: String,
  ): List<String> {
    val errors = mutableListOf<String>()

    if (motiveId.isBlank()) errors.add("MotiveId is required")
    if (placeId.isBlank()) errors.add("PlaceId is required")
    if (categoryId.isBlank()) errors.add("CategoryId is required")
    if (eventOwnerId.isBlank()) errors.add("EventOwnerId is required")
    if (albumTitle.isBlank()) errors.add("AlbumTitle is required")

    if (placeRepository.findByName(placeId) == null) {
      errors.add("Place with name $placeId does not exist")
    }
    if (categoryRepository.findByName(categoryId) == null) {
      errors.add("Category with name $categoryId does not exist")
    }
    if (eventOwnerRepository.findByEventOwnerName(
        EventOwnerName.valueOf(eventOwnerId),
      ) == null
    ) {
      errors.add("EventOwner with name $eventOwnerId does not exist")
    }
    if (photoGangBangerRepository.findById(photoGangBangerId) == null) {
      errors.add("PhotoGangBanger with id $photoGangBangerId does not exist")
    }
    if (albumRepository.findByTitle(albumTitle) == null) {
      errors.add("Album with title $albumTitle does not exist")
    }

    return errors.toList()
  }

  fun findById(
    id: UUID,
    allowedSecurityLevels: List<String> = listOf("ALLE"),
  ): PhotoDto = photoRepository.findById(id, allowedSecurityLevels) ?: throw EntityNotFoundException("Photo $id not found")

  fun getByMotiveId(
    id: UUID,
    page: Int,
    pageSize: Int,
    allowedSecurityLevels: List<String> = listOf("ALLE"),
  ): Page<PhotoDto> = photoRepository.findByMotiveId(id, page, pageSize, allowedSecurityLevels)

  fun getAll(
    page: Int,
    pageSize: Int,
    motive: UUID,
    tag: List<String>,
    fromDate: LocalDate,
    toDate: LocalDate,
    category: String,
    place: UUID,
    isGoodPic: Boolean,
    album: UUID,
    sortBy: String,
    desc: Boolean,
    allowedSecurityLevels: List<String>,
    isAnalog: Boolean,
  ): Page<PhotoDto> =
    photoRepository.findAll(
      page = page,
      pageSize = pageSize,
      motive = motive,
      tag = tag,
      fromDate = fromDate,
      toDate = toDate,
      category = category,
      place = place,
      isGoodPic = isGoodPic,
      album = album,
      sortBy = sortBy,
      desc = desc,
      allowedSecurityLevels = allowedSecurityLevels,
      isAnalog = isAnalog,
    )

  fun getGoodPhotos(
    page: Int,
    pageSize: Int,
    allowedSecurityLevels: List<String> = listOf("ALLE"),
  ): Page<PhotoDto> = photoRepository.findGoodPhotos(page, pageSize, allowedSecurityLevels)

  fun getAllAnalogPhotos(
    page: Int,
    pageSize: Int,
  ): Page<PhotoDto> = photoRepository.findAllAnalogPhotos(page, pageSize)

  fun getAllDigitalPhotos(
    page: Int,
    pageSize: Int,
    motive: UUID,
    tag: List<String>,
    fromDate: LocalDate,
    toDate: LocalDate,
    category: String,
    place: UUID,
    isGoodPic: Boolean,
    album: UUID,
    sortBy: String,
    desc: Boolean,
    allowedSecurityLevels: List<String> = listOf("ALLE"),
    isAnalog: Boolean = false,
  ): Page<PhotoDto> =
    photoRepository.findAllDigitalPhotos(
      page = page,
      pageSize = pageSize,
      motive = motive,
      tag = tag,
      fromDate = fromDate,
      toDate = toDate,
      category = category,
      place = place,
      isGoodPic = isGoodPic,
      album = album,
      sortBy = sortBy,
      desc = desc,
      allowedSecurityLevels = allowedSecurityLevels,
    )

  fun patch(dto: PhotoPatchRequestDto): PhotoDto {
    val photoTags =
      dto.photoTags?.mapNotNull { photoTagRepository.findByName(it) }
        ?: emptyList()
    return photoRepository.patch(dto, photoTags)
  }

  fun createNewMotiveAndSaveDigitalPhotos(
    motiveString: String,
    placeString: String,
    eventOwnerString: String,
    securityLevel: String,
    albumTitle: String,
    photoGangBangerId: UUID,
    smallUrl: String,
    mediumUrl: String,
    largeUrl: String,
    tagList: List<String>,
    categoryName: String,
    isGoodPhoto: Boolean,
    dateTaken: LocalDate,
  ): List<PhotoDto> {
    val album =
      albumRepository.findByTitle(albumTitle)
        ?: throw EntityNotFoundException("Album '$albumTitle' not found")
    val photoGangBanger =
      photoGangBangerRepository.findById(photoGangBangerId)
        ?: throw EntityNotFoundException(
          "PhotoGangBanger $photoGangBangerId not found",
        )
    val place =
      placeRepository.findByName(placeString)
        ?: throw EntityNotFoundException("Place '$placeString' not found")
    val eventOwner =
      eventOwnerRepository.findByEventOwnerName(
        EventOwnerName.valueOf(eventOwnerString),
      )
        ?: throw EntityNotFoundException(
          "EventOwner '$eventOwnerString' not found",
        )
    val category =
      categoryRepository.findByName(categoryName)
        ?: throw EntityNotFoundException(
          "Category '$categoryName' not found",
        )
    val motive =
      motiveRepository.findByTitle(motiveString)?.toDto()
        ?: motiveRepository.create(
          MotiveDto(
            title = motiveString,
            categoryDto = category,
            eventOwnerDto = eventOwner,
            albumDto = album,
            dateCreated = dateTaken,
          ),
        )
    val photoTags = tagList.mapNotNull { photoTagRepository.findByName(it) }
    val securityLevelDto = SecurityLevelDto(SecurityLevelType.valueOf(securityLevel))

    val photoDto =
      PhotoDto(
        isGoodPicture = isGoodPhoto,
        smallUrl = smallUrl,
        mediumUrl = mediumUrl,
        largeUrl = largeUrl,
        motive = motive,
        placeDto = place,
        gang = null,
        securityLevel = securityLevelDto,
        albumDto = album,
        photoTags = photoTags,
        categoryDto = category,
        photoGangBangerDto = photoGangBanger,
        dateTaken = dateTaken,
      )
    photoRepository.createPhoto(photoDto)
    return listOf(photoDto)
  }

  fun saveDigitalPhotos(
    isGoodPictureList: List<Boolean>,
    motiveIdList: List<UUID>,
    placeIdList: List<UUID>,
    securityLevelList: List<String>,
    gangIdList: List<UUID>,
    photoGangBangerIdList: List<UUID>,
    albumIdList: List<UUID>,
    categoryIdList: List<UUID>,
    fileNameList: List<String>,
    dateTaken: LocalDate,
  ): List<PhotoDto> =
    fileNameList.indices.map { i ->
      val motive =
        motiveRepository.findById(motiveIdList[i])
          ?: throw EntityNotFoundException(
            "Motive ${motiveIdList[i]} not found",
          )
      val place =
        placeRepository.findById(placeIdList[i])
          ?: throw EntityNotFoundException(
            "Place ${placeIdList[i]} not found",
          )
      val album =
        albumRepository.findById(albumIdList[i])
          ?: throw EntityNotFoundException(
            "Album ${albumIdList[i]} not found",
          )
      val category =
        categoryRepository.findById(categoryIdList[i])
          ?: throw EntityNotFoundException(
            "Category ${categoryIdList[i]} not found",
          )
      val photoGangBanger =
        photoGangBangerRepository.findById(photoGangBangerIdList[i])
          ?: throw EntityNotFoundException(
            "PhotoGangBanger ${photoGangBangerIdList[i]} not found",
          )
      val securityLevelDto =
        SecurityLevelDto(SecurityLevelType.valueOf(securityLevelList[i]))

      val photoDto =
        PhotoDto(
          isGoodPicture = isGoodPictureList[i],
          smallUrl = fileNameList[i],
          mediumUrl = fileNameList[i],
          largeUrl = fileNameList[i],
          motive = motive,
          placeDto = place,
          gang = null,
          securityLevel = securityLevelDto,
          albumDto = album,
          photoTags = emptyList(),
          categoryDto = category,
          photoGangBangerDto = photoGangBanger,
          dateTaken = dateTaken,
        )
      photoRepository.createPhoto(photoDto)
      photoDto
    }

  fun deletePhoto(photoId: UUID) {
    TODO("Not yet implemented")
  }
}

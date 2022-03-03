package no.fg.hilflingbackend.service

import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.PhotoDto
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate
import java.util.UUID

interface IBaseService<T> {
  fun getById(id: UUID): PhotoDto?
  fun getAll(
    page: Int = 0,
    pageSize: Int = 100,
    motive: UUID = UUID(0L, 0L),
    tag: List<String> = listOf<String>(),
    fromDate: LocalDate,
    toDate: LocalDate,
    category: String,
    place: UUID,
    isGoodPic: Boolean = false,
    album: String,
    sortBy: String,
    desc: Boolean = true
  ): Page<T>
}
interface IPhotoService : IBaseService<PhotoDto> {
  fun saveDigitalPhotos(
    isGoodPictureList: List<Boolean>,
    motiveIdList: List<UUID>,
    placeIdList: List<UUID>,
    securityLevelIdList: List<UUID>,
    gangIdList: List<UUID>,
    photoGangBangerIdList: List<UUID>,
    albumIdList: List<UUID>,
    categoryIdList: List<UUID>,
    fileList: List<MultipartFile>
  ): List<String>
  fun saveAnalogPhotos(
    isGoodPictureList: List<Boolean>,
    motiveIdList: List<UUID>,
    placeIdList: List<UUID>,
    securityLevelIdList: List<UUID>,
    gangIdList: List<UUID>,
    photoGangBangerIdList: List<UUID>,
    fileList: List<MultipartFile>
  ): List<String>
  fun createNewMotiveAndSaveDigitalPhotos(
    motiveString: String,
    placeString: String,
    securityLevelId: UUID,
    photoGangBangerId: UUID,
    albumId: UUID,
    categoryName: String,
    eventOwnerString: String,
    photoFileList: List<MultipartFile>,
    isGoodPhotoList: List<Boolean>,
    tagList: List<List<String>>
  ): List<String>
  fun getCarouselPhotos(page: Int = 0, pageSize: Int = 100): Page<PhotoDto>
  fun getAllAnalogPhotos(page: Int = 0, pageSize: Int = 100): Page<PhotoDto> // TODO: Need different DTO for analog
  fun getAllDigitalPhotos(
    page: Int = 0,
    pageSize: Int = 100,
    motive: UUID = UUID(0L, 0L),
    tag: List<String> = listOf<String>(),
    fromDate: LocalDate,
    toDate: LocalDate,
    category: String,
    place: UUID = UUID(0L, 0L),
    isGoodPic: Boolean = false,
    album: String,
    sortBy: String,
    desc: Boolean = true
  ): Page<PhotoDto>
}

package no.fg.hilflingbackend.service

import no.fg.hilflingbackend.dto.PhotoDto
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

interface IBaseService<T> {
  fun getById(id: UUID): PhotoDto?
  fun getAll(): List<T>
}
interface IPhotoService : IBaseService<PhotoDto> {
  fun saveDigitalPhotos(
    isGoodPictureList: List<Boolean>,
    motiveIdList: List<UUID>,
    placeIdList: List<UUID>,
    securityLevelIdList: List<UUID>,
    gangIdList: List<UUID>,
    photoGangBangerIdList: List<UUID>,
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
  fun getCarouselPhotos(): List<PhotoDto>
  fun getAllAnalogPhotos(): List<PhotoDto> // TODO: Need different DTO for analog
  fun getAllDigitalPhotos(): List<PhotoDto>
}

package no.fg.hilflingbackend.service

import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.dto.PhotoPatchRequestDto
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

interface IBaseService<T> {
  fun findById(id: UUID): PhotoDto?
  fun getAll(page: Int = 0, pageSize: Int = 100): Page<T>
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
    tagList: List<String>,
  ): List<String>
  fun getCarouselPhotos(page: Int = 0, pageSize: Int = 100): Page<PhotoDto>
  fun getAllAnalogPhotos(page: Int = 0, pageSize: Int = 100): Page<PhotoDto> // TODO: Need different DTO for analog
  fun getAllDigitalPhotos(page: Int = 0, pageSize: Int = 100): Page<PhotoDto>
  fun patch(dto: PhotoPatchRequestDto): PhotoDto
}

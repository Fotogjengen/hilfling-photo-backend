package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.drop
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.take
import me.liuwj.ktorm.entity.toList
import me.liuwj.ktorm.entity.update
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.dto.PhotoTagDto
import no.fg.hilflingbackend.dto.PhotoTagId
import no.fg.hilflingbackend.model.AnalogPhoto
import no.fg.hilflingbackend.model.Photo
import no.fg.hilflingbackend.model.PhotoTagReferences
import no.fg.hilflingbackend.model.PhotoTags
import no.fg.hilflingbackend.model.SecurityLevel
import no.fg.hilflingbackend.model.SecurityLevels
import no.fg.hilflingbackend.model.albums
import no.fg.hilflingbackend.model.analog_photos
import no.fg.hilflingbackend.model.photos
import no.fg.hilflingbackend.model.toDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.UUID

@Repository
open class PhotoRepository(
  val database: Database
) {
  val logger = LoggerFactory.getLogger(this::class.java)

  private fun findCorrespondingPhotoTagDtos(photo: Photo): List<PhotoTagDto> {
    return database.from(PhotoTags)
      .crossJoin(PhotoTagReferences)
      .select(
        PhotoTags.name,
        PhotoTags.id,
        PhotoTagReferences.photoId,
        PhotoTagReferences.photoTagId
      )
      .where { PhotoTagReferences.photoId eq photo.id }
      .map { row ->
        PhotoTagDto(
          photoTagId = PhotoTagId(row[PhotoTags.id]!!),
          name = row[PhotoTags.name]!!
        )
      }
  }

  fun findById(id: UUID): PhotoDto? {
    return database.photos.find { it.id eq id }
      ?.let { photo ->
        return photo.toDto(
          findCorrespondingPhotoTagDtos(photo)
        )
      }
  }

  fun findByMotiveId(id: UUID, page: Int, pageSize: Int): Page<PhotoDto>? {
    val photos = database.photos.filter {
      it.motiveId eq id
    }
    val photoDtos = photos.toList()
      .map { it.toDto(findCorrespondingPhotoTagDtos(it)) }
    return Page(
      page = page,
      pageSize = pageSize,
      totalRecords = photos.totalRecords,
      currentList = photoDtos
    )
  }

  fun findAnalogPhotoById(id: UUID): AnalogPhoto? {
    return database.analog_photos.find { it.id eq id }
  }

  fun findAll(page: Int, pageSize: Int): Page<PhotoDto> {
    val photos = database.photos

    val photoDtos = photos.drop(page).take(pageSize).toList()
      .map {
        it.toDto(
          findCorrespondingPhotoTagDtos(it)
        )
      }
    return Page(
      page = page,
      pageSize = pageSize,
      totalRecords = photos.totalRecords,
      currentList = photoDtos
    )
  }

  fun findAllAnalogPhotos(page: Int, pageSize: Int): Page<PhotoDto> {
    val analogAlbums = database.albums
      .filter { it.isAnalog eq true }

    val photos = analogAlbums.toList().map { album ->
      database.photos.filter {
        it.albumId eq album.id
      }
    }

    val totalRecords = photos.sumOf { it.totalRecords }

    val photoDtos = photos.map { photoList ->
      photoList.drop(page).take(pageSize).toList()
        .map { it.toDto(findCorrespondingPhotoTagDtos(it)) }
    }.flatten()

    return Page(
      page = page,
      pageSize = pageSize,
      totalRecords = totalRecords,
      currentList = photoDtos
    )
  }

  fun findAllDigitalPhotos(
    page: Int,
    pageSize: Int,
    motive: UUID,
    tag: List<String>,
    fromDate: LocalDate,
    toDate: LocalDate,
    category: String,
    place: String,
    isGoodPic: Boolean,
    album: String,
    sortBy: String,
    desc: Boolean): Page<PhotoDto> {
    val digitalAlbums = database.albums
      .filter { it.isAnalog eq false }

    print(motive)
    val photos = digitalAlbums.toList().map { album ->
      database.photos.filter {
        it.albumId eq album.id
      }//.filter { it.motiveId eq motive }
    }

    val totalRecords = photos.sumOf { it.totalRecords }

    val photoDtos = photos.map { photoList ->
      photoList.drop(page).take(pageSize).toList()
        .map { it.toDto(findCorrespondingPhotoTagDtos(it)) }
    }.flatten()

    return Page(
      page = page,
      pageSize = pageSize,
      totalRecords = totalRecords,
      currentList = photoDtos
    )
  }

  fun findCarouselPhotos(page: Int, pageSize: Int): Page<PhotoDto> {
    val photos = database
      .photos
      .filter { it.isGoodPicture eq true }
    val photoDtos = photos.drop(page).take(pageSize).toList()
      .map { it.toDto(findCorrespondingPhotoTagDtos(it)) }

    return Page(
      page = page,
      pageSize = pageSize,
      totalRecords = photos.totalRecords,
      currentList = photoDtos
    )
  }

  fun findBySecurityLevel(
    securityLevel: SecurityLevel,
    page: Int,
    pageSize: Int
  ): Page<PhotoDto> {
    val photos = database
      .photos
      .filter {
        val securityLevelFromDatabase = it.securityLevelId.referenceTable as SecurityLevels
        securityLevelFromDatabase.id eq securityLevel.id
      }
    val photoDtos = photos.drop(page).take(pageSize).toList()
      .map { it.toDto(findCorrespondingPhotoTagDtos(it)) }

    return Page(
      page = page,
      pageSize = pageSize,
      totalRecords = photos.totalRecords,
      currentList = photoDtos
    )
  }

  fun createPhoto(
    photoDto: PhotoDto
  ): Int {
    logger.info("Storing photo ${photoDto.photoId.id} to database")

    val numOfSavedPhotos = database.photos.add(photoDto.toEntity())
    // TODO: Rewrite to batchInsert for perfomance gains
    // TODO: Add photoTags as well
    /*
    photoDto.photoTags.forEach { photoTagDto: PhotoTagDto ->
      logger.info("Adding photoTag ${photoTagDto.name} to ${photoDto.photoId.id}")
      database.insert(PhotoTagReferences) {
        set(it.id, UUID.randomUUID())
        set(it.photoId, photoDto.photoId.id)
        set(it.photoTagId, photoTagDto.photoTagId.id)
      }
    }
     */
    logger.info("Storing photo tags to database")
    val photoTagDtoList = photoDto.photoTags
    database.batchInsert(PhotoTagReferences) {
      photoTagDtoList.map { photoTagDto ->
        item {
          set(it.id, UUID.randomUUID())
          set(it.photoTagId, photoTagDto.photoTagId.id)
          set(it.photoId, photoDto.photoId.id)
        }
      }
    }

    return numOfSavedPhotos
  }

  fun createAnalogPhoto(
    analogPhoto: AnalogPhoto
  ): AnalogPhoto {
    TODO("Implement")
    /*
    val photoFromDatabase = createPhoto(
      analogPhoto.photo
    )
    val analogPhotoFromDatabase = AnalogPhoto {
      this.imageNumber = analogPhoto.imageNumber
      this.pageNumber = analogPhoto.pageNumber
      this.photo = analogPhoto.photo
    }
    database.analog_photos.add(analogPhotoFromDatabase)
    return analogPhotoFromDatabase

     */
  }

  fun patchAnalogPhoto(
    analogPhoto: AnalogPhoto
  ): AnalogPhoto? {
    database
      .analog_photos
      .update(analogPhoto)
    return findAnalogPhotoById(analogPhoto.id)
  }
}

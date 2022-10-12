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
import no.fg.hilflingbackend.dto.*
import no.fg.hilflingbackend.model.*
import no.fg.hilflingbackend.value_object.Email
import no.fg.hilflingbackend.value_object.PhoneNumber
import no.fg.hilflingbackend.value_object.SecurityLevelType
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.UUID
import javax.persistence.EntityNotFoundException

@Repository
open class PhotoRepository(
  val database: Database
) {
  val logger = LoggerFactory.getLogger(this::class.java)

  fun findCorrespondingPhotoTagDtos(photoId: UUID): List<PhotoTagDto> {
    return database.from(PhotoTags)
      .innerJoin(PhotoTagReferences, on = PhotoTags.id eq PhotoTagReferences.photoTagId)
      .select(
        PhotoTags.name,
        PhotoTags.id,
        PhotoTagReferences.photoId,
        PhotoTagReferences.photoTagId
      )
      .where { PhotoTagReferences.photoId eq photoId }
      .map { row ->
        PhotoTagDto(
          photoTagId = PhotoTagId(row[PhotoTags.id]!!),
          name = row[PhotoTags.name]!!
        )
      }
  }

  fun constructPhotoDto(row: QueryRowSet): PhotoDto {
    return PhotoDto(
      photoId = PhotoId(row[Photos.id]!!),
      isGoodPicture = row[Photos.isGoodPicture]!!,
      smallUrl = row[Photos.smallUrl]!!,
      mediumUrl = row[Photos.mediumUrl]!!,
      largeUrl = row[Photos.largeUrl]!!,
      motive = MotiveDto(
        motiveId = MotiveId(),
        title = row[Motives.title]!!,
        categoryDto = CategoryDto(
          categoryId = CategoryId(row[Categories.id]!!),
          name = row[Categories.name]!!
        ),
        eventOwnerDto = EventOwnerDto(
          eventOwnerId = EventOwnerId(row[EventOwners.id]!!),
          name = EventOwnerName.valueOf(row[EventOwners.name]!!)
        ),
        albumDto = AlbumDto(
          albumId = AlbumId(row[Albums.id]!!),
          isAnalog = row[Albums.isAnalog]!!,
          title = row[Albums.title]!!
        ),
      ),
      placeDto = PlaceDto(
        placeId = PlaceId(row[Places.id]!!),
        name = row[Places.name]!!
      ),
      securityLevel = SecurityLevelDto(
        securityLevelId = SecurityLevelId(row[SecurityLevels.id]!!),
        securityLevelType = SecurityLevelType.valueOf(row[SecurityLevels.type]!!)
      ),
      gang = GangDto(
        GangId(row[Gangs.id]!!),
        row[Gangs.name]!!
      ),
      albumDto = AlbumDto(
        AlbumId(row[Albums.id]!!),
        row[Albums.title]!!,
        row[Albums.isAnalog]!!
      ),
      categoryDto = CategoryDto(
        CategoryId(row[Categories.id]!!),
        row[Categories.name]!!,
      ),
      photoGangBangerDto = PhotoGangBangerDto(
        PhotoGangBangerId(row[PhotoGangBangers.id]!!),
        RelationshipStatus.valueOf(row[PhotoGangBangers.relationshipStatus]!!),
        SemesterStart(row[PhotoGangBangers.semesterStart]!!),
        row[PhotoGangBangers.isActive]!!,
        row[PhotoGangBangers.isPang]!!,
        row[PhotoGangBangers.address]!!,
        row[PhotoGangBangers.zipCode]!!,
        row[PhotoGangBangers.city]!!,
        SamfundetUserDto(
          SamfundetUserId(row[SamfundetUsers.id]!!),
          row[SamfundetUsers.firstName]!!,
          row[SamfundetUsers.lastName]!!,
          row[SamfundetUsers.username]!!,
          PhoneNumber(row[SamfundetUsers.phoneNumber]!!),
          Email(row[SamfundetUsers.email]!!),
          row[SamfundetUsers.profilePicture]!!,
          row[SamfundetUsers.sex]!!,
          SecurityLevelDto(
            SecurityLevelId(row[SamfundetUsers.securityLevelId]!!)
          )
        ),
        PositionDto(
          PositionId(row[Positions.id]!!),
          row[Positions.title]!!,
          Email(row[Positions.email]!!)
        )
      ),
      photoTags = findCorrespondingPhotoTagDtos(row[Photos.id]!!)
    )
  }

  fun findById(id: UUID): PhotoDto? {
    return database.photos.find { it.id eq id }
      ?.let { photo ->
        return photo.toDto(
          findCorrespondingPhotoTagDtos(id)
        )
      }
  }

  fun findByMotiveId(id: UUID, page: Int, pageSize: Int): Page<PhotoDto>? {
    val photos = database.photos.filter {
      it.motiveId eq id
    }
    val photoDtos = photos.toList()
      .map { it.toDto(findCorrespondingPhotoTagDtos(it.id)) }
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

  fun findAll(
    page: Int = 0,
    pageSize: Int = 100,
    motive: UUID,
    tag: List<String> = listOf<String>(),
    fromDate: LocalDate,
    toDate: LocalDate,
    category: String,
    place: UUID,
    isGoodPic: Boolean = false,
    album: UUID,
    sortBy: String,
    desc: Boolean = true
  ): Page<PhotoDto> {
    val photos = database.photos

    val ph = database.from(Photos)
      .innerJoin(Motives, on = Photos.motiveId eq Motives.id)
      .innerJoin(Places, on = Photos.placeId eq Places.id)
      .innerJoin(Albums, on = Photos.albumId eq Albums.id)
      .innerJoin(Categories, on = Motives.categoryId eq Categories.id)
      .innerJoin(EventOwners, on = Motives.eventOwnerId eq EventOwners.id)
      .innerJoin(SecurityLevels, on = Photos.securityLevelId eq SecurityLevels.id)
      .innerJoin(Gangs, on = Photos.gangId eq Gangs.id)
      .innerJoin(PhotoGangBangers, on = Photos.photoGangBangerId eq PhotoGangBangers.id)
      .innerJoin(SamfundetUsers, on = PhotoGangBangers.samfundetUserId eq SamfundetUsers.id)
      .innerJoin(Positions, on = PhotoGangBangers.positionId eq Positions.id)
      .select(
        Photos.id,
        Photos.isGoodPicture,
        Photos.smallUrl,
        Photos.mediumUrl,
        Photos.largeUrl,
        Motives.id,
        Motives.title,
        Albums.id,
        Albums.isAnalog,
        Albums.title,
        Categories.id,
        Categories.name,
        EventOwners.id,
        EventOwners.name,
        SamfundetUsers.id,
        SamfundetUsers.firstName,
        SamfundetUsers.lastName,
        SamfundetUsers.username,
        SamfundetUsers.phoneNumber,
        SamfundetUsers.email,
        SamfundetUsers.profilePicture,
        SamfundetUsers.sex,
        SamfundetUsers.securityLevelId,
        Places.id,
        Places.name,
        SecurityLevels.id,
        SecurityLevels.type,
        Gangs.id,
        Gangs.name,
        PhotoGangBangers.id,
        PhotoGangBangers.semesterStart,
        PhotoGangBangers.relationshipStatus,
        PhotoGangBangers.isActive,
        PhotoGangBangers.isPang,
        PhotoGangBangers.address,
        PhotoGangBangers.zipCode,
        PhotoGangBangers.city,
        Positions.id,
        Positions.title,
        Positions.email,
      )
      .where {
        if(motive != UUID(0L, 0L)){
          Motives.id eq motive
        } else {
          Motives.id notEq UUID(0L, 0L)
        }
      }.where {
        if(album != UUID(0L, 0L)){
          System.out.println("test")
          Albums.id eq album
        } else {
          Albums.id notEq UUID(0L, 0L)
        }
      }
      .map { row -> constructPhotoDto(row) }

    // TODO: Use limit instead ;)
    val photoDtos = photos.drop(page).take(pageSize).toList()
      .map {
        it.toDto(
          findCorrespondingPhotoTagDtos(it.id)
        )
    }

    return Page(
      page = page,
      pageSize = pageSize,
      totalRecords = photos.totalRecords,
      currentList = ph
    )
  }

  private fun calculateNewUrls(dto: PhotoDto, patchDto: PhotoPatchRequestDto): Triple<String, String, String> {
    // TODO: calculate correct URLS
    return Triple(dto.smallUrl, dto.mediumUrl, dto.largeUrl)
  }

  fun patch(dto: PhotoPatchRequestDto, photoTags: List<PhotoTagDto>?): PhotoDto {
    val fromDb = findById(dto.photoId.id)
      ?: throw EntityNotFoundException("Could not find Photo")
    val (smallUrl, mediumUrl, largeUrl) = calculateNewUrls(fromDb, dto)

    if (photoTags != null) {
      try {
        database.batchInsert(PhotoTagReferences) {
          photoTags.map { photoTagDto ->
            item {
              set(it.id, UUID.randomUUID())
              set(it.photoTagId, photoTagDto.photoTagId.id)
              set(it.photoId, dto.photoId.id)
            }
          }
        }
      } catch (e: Exception) {
        logger.info(String.format("Tried to create a PhotoTagReference that already existed. Ignoring error.", e.message))
      }
    }

    val newDto = PhotoDto(
      photoId = fromDb.photoId,
      isGoodPicture = dto.isGoodPicture ?: fromDb.isGoodPicture,
      smallUrl = smallUrl,
      mediumUrl = mediumUrl,
      largeUrl = largeUrl,
      motive = dto.motive ?: fromDb.motive,
      placeDto = dto.placeDto ?: fromDb.placeDto,
      securityLevel = dto.securityLevel ?: fromDb.securityLevel,
      gang = dto.gang ?: fromDb.gang,
      albumDto = dto.albumDto ?: fromDb.albumDto,
      categoryDto = dto.categoryDto ?: fromDb.categoryDto,
      photoGangBangerDto = dto.photoGangBangerDto ?: fromDb.photoGangBangerDto,
      photoTags = photoTags ?: fromDb.photoTags
    )
    val updated = database.photos.update(newDto.toEntity())

    return if (updated == 1) newDto else fromDb
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
        .map { it.toDto(findCorrespondingPhotoTagDtos(it.id)) }
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
    place: UUID,
    isGoodPic: Boolean,
    album: UUID,
    sortBy: String,
    desc: Boolean): Page<PhotoDto> {
    val photos = database.photos

    val ph = database.from(Photos)
      .innerJoin(Motives, on = Photos.motiveId eq Motives.id)
      .innerJoin(Places, on = Photos.placeId eq Places.id)
      .innerJoin(Albums, on = Photos.albumId eq Albums.id)
      .innerJoin(Categories, on = Motives.categoryId eq Categories.id)
      .innerJoin(EventOwners, on = Motives.eventOwnerId eq EventOwners.id)
      .innerJoin(SecurityLevels, on = Photos.securityLevelId eq SecurityLevels.id)
      .innerJoin(Gangs, on = Photos.gangId eq Gangs.id)
      .innerJoin(PhotoGangBangers, on = Photos.photoGangBangerId eq PhotoGangBangers.id)
      .innerJoin(SamfundetUsers, on = PhotoGangBangers.samfundetUserId eq SamfundetUsers.id)
      .innerJoin(Positions, on = PhotoGangBangers.positionId eq Positions.id)
      .select(
        Photos.id,
        Photos.isGoodPicture,
        Photos.smallUrl,
        Photos.mediumUrl,
        Photos.largeUrl,
        Motives.id,
        Motives.title,
        Albums.id,
        Albums.isAnalog,
        Albums.title,
        Categories.id,
        Categories.name,
        EventOwners.id,
        EventOwners.name,
        SamfundetUsers.id,
        SamfundetUsers.firstName,
        SamfundetUsers.lastName,
        SamfundetUsers.username,
        SamfundetUsers.phoneNumber,
        SamfundetUsers.email,
        SamfundetUsers.profilePicture,
        SamfundetUsers.sex,
        SamfundetUsers.securityLevelId,
        Places.id,
        Places.name,
        SecurityLevels.id,
        SecurityLevels.type,
        Gangs.id,
        Gangs.name,
        PhotoGangBangers.id,
        PhotoGangBangers.semesterStart,
        PhotoGangBangers.relationshipStatus,
        PhotoGangBangers.isActive,
        PhotoGangBangers.isPang,
        PhotoGangBangers.address,
        PhotoGangBangers.zipCode,
        PhotoGangBangers.city,
        Positions.id,
        Positions.title,
        Positions.email,
      )
      .where { Albums.isAnalog eq false }
      .where {
        if(motive != UUID(0L, 0L)){
          Motives.id eq motive
        } else {
          Motives.id notEq UUID(0L, 0L)
        }
      }.where {
        if(motive != UUID(0L, 0L)){
          Albums.id eq album
        } else {
          Albums.id notEq UUID(0L, 0L)
        }
      }
      .map { row -> constructPhotoDto(row) }

    val photoDtos = photos.drop(page).take(pageSize).toList()
      .map {
        it.toDto(
          findCorrespondingPhotoTagDtos(it.id)
        )
      }

    return Page(
      page = page,
      pageSize = pageSize,
      totalRecords = photos.totalRecords,
      currentList = ph
    )
  }

  fun findCarouselPhotos(page: Int, pageSize: Int): Page<PhotoDto> {
    val photos = database
      .photos
      .filter { it.isGoodPicture eq true }
    val photoDtos = photos.drop(page).take(pageSize).toList()
      .map { it.toDto(findCorrespondingPhotoTagDtos(it.id)) }

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
      .map { it.toDto(findCorrespondingPhotoTagDtos(it.id)) }

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

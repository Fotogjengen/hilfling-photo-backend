package no.fg.hilflingbackend.service

import no.fg.hilflingbackend.configurations.ImageFileStorageProperties
import no.fg.hilflingbackend.dto.AlbumDto
import no.fg.hilflingbackend.dto.CategoryDto
import no.fg.hilflingbackend.dto.EventOwnerDto
import no.fg.hilflingbackend.dto.EventOwnerName
import no.fg.hilflingbackend.dto.GangDto
import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.dto.PhotoTagDto
import no.fg.hilflingbackend.dto.PlaceDto
import no.fg.hilflingbackend.dto.SecurityLevelDto
import no.fg.hilflingbackend.model.toDto
import no.fg.hilflingbackend.repository.AlbumRepository
import no.fg.hilflingbackend.repository.CategoryRepository
import no.fg.hilflingbackend.repository.EventOwnerRepository
import no.fg.hilflingbackend.repository.GangRepository
import no.fg.hilflingbackend.repository.MotiveRepository
import no.fg.hilflingbackend.repository.PhotoGangBangerRepository
import no.fg.hilflingbackend.repository.PhotoRepository
import no.fg.hilflingbackend.repository.PhotoTagRepository
import no.fg.hilflingbackend.repository.PlaceRepository
import no.fg.hilflingbackend.repository.SecurityLevelRepository
import no.fg.hilflingbackend.utils.convertToValidFolderName
import no.fg.hilflingbackend.value_object.ImageFileName
import no.fg.hilflingbackend.value_object.PhotoSize
import no.fg.hilflingbackend.value_object.SecurityLevelType
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.UUID
import java.util.stream.Stream
import javax.persistence.EntityNotFoundException

@Service
class PhotoService(
  val imageFileStorageProperties: ImageFileStorageProperties,
  val environment: Environment,
  val photoRepository: PhotoRepository,
  val gangRepository: GangRepository,
  val photoTagRepository: PhotoTagRepository,
  val motiveRepository: MotiveRepository,
  val eventOwnerRepository: EventOwnerRepository,
  val placeRepository: PlaceRepository,
  val categoryRepository: CategoryRepository,
  val albumRepository: AlbumRepository,
  val securityLevelRepository: SecurityLevelRepository,
  val photoGangBangerRepository: PhotoGangBangerRepository,
) : IPhotoService {

  val logger = LoggerFactory.getLogger(this::class.java)
  val profileLocation = Paths.get("filestorage/PROFILE")

  val rootLocation = Paths.get("static-files/static/img/")
  val photoGangBangerLocation = Paths.get("static-files/static/img/FG")
  val houseMemberLocation = Paths.get("static-files/static/img/HUSFOLK")
  val allLocation = Paths.get("static-files/static/img/ALLE")

  fun savePhotosToDisk() {
    throw NotImplementedError()
  }

  /**
   * FilePath is generated as follows:
   * basepath/<securityLevel>/<Album>/<Motive/<uuuid>.jpg
   */
  fun generateFilePath(
    fileName: ImageFileName,
    securityLevel: SecurityLevelDto,
    motive: MotiveDto,
    size: PhotoSize,
  ): Path {
    // BasePath
    val basePath = imageFileStorageProperties.savedPhotosPath
    println("BaseBath from config: $basePath")
    val fullFilePath = Paths.get(
      "$basePath/" +
        "${securityLevel.securityLevelType}/" +
        "${convertToValidFolderName(motive.albumDto.title)}/" +
        "${convertToValidFolderName(motive.title)}" +
        "$size-${fileName.filename}"
    )
    println(fullFilePath)
    // TODO: Check if directories exiist before continou
    if (!Files.isDirectory(fullFilePath)) throw IllegalStateException("The file path does not exist")
    return fullFilePath
  }

  fun store(file: MultipartFile, photoDto: PhotoDto, imageFileName: ImageFileName): String {
    // Create filePath

    val directory = when (photoDto.securityLevel.securityLevelType) {
      SecurityLevelType.FG -> this.photoGangBangerLocation.resolve(photoDto.motive.motiveId.toString())
      SecurityLevelType.HUSFOLK -> this.houseMemberLocation.resolve(photoDto.motive.motiveId.toString())
      SecurityLevelType.ALLE -> this.allLocation.resolve(photoDto.motive.motiveId.toString())
      SecurityLevelType.PROFILE -> this.profileLocation.resolve(photoDto.motive.motiveId.toString())
      else -> throw IllegalArgumentException("Invalid security level")
    }
    if (!Files.isDirectory(directory)) {
      Files.createDirectories(directory)
    }
    val location = directory.resolve(imageFileName.filename)
    Files.copy(file.inputStream, location).toString()

    return location.toString().subSequence(20, location.toString().length).toString()
  }

  // TODO: Not used, remove?
  fun loadFile(filename: String): Resource {
    val file = rootLocation.resolve(filename)
    val resource = UrlResource(file.toUri())

    if (resource.exists() || resource.isReadable) {
      return resource
    } else {
      throw RuntimeException("Fail!")
    }
  }

  // TODO: REMOVE FROM PROD
  fun createFilesystemIfNotExists() {
    logger.info("Deletes and recreates img static files location")
    FileSystemUtils.deleteRecursively(rootLocation) // TODO remove, deletes everything
    if (!Files.exists(rootLocation)) {
      Files.createDirectory(rootLocation)
    }
    if (!Files.exists(photoGangBangerLocation)) {
      Files.createDirectory(photoGangBangerLocation)
    }
    if (!Files.exists(houseMemberLocation)) {
      Files.createDirectory(houseMemberLocation)
    }
    if (!Files.exists(allLocation)) {
      Files.createDirectory(allLocation)
    }
    if (!Files.exists(profileLocation)) {
      Files.createDirectory(profileLocation)
    }
  }

  fun loadFiles(): Stream<Path> {
    return Files.walk(this.rootLocation, 1)
      .filter { path -> path != this.rootLocation }
      .map(this.rootLocation::relativize)
  }

  override fun saveDigitalPhotos(
    isGoodPictureList: List<Boolean>,
    motiveIdList: List<UUID>,
    placeIdList: List<UUID>,
    securityLevelIdList: List<UUID>,
    gangIdList: List<UUID>,
    photoGangBangerIdList: List<UUID>,
    albumIdList: List<UUID>,
    categoryIdList: List<UUID>,
    fileList: List<MultipartFile>
  ): List<String> {

    return fileList.mapIndexed {
      index, file ->
      /*
      val cachedMotive = { id: UUID ->
        motiveRepository
          .findById(id)
          ?: throw EntityNotFoundException("Did not find motive")
      }.memoize()

       */

      // TODO: Test speed
      /*
      val cachedPlace = { id: UUID ->
        runBlocking {
          placeRepository
            .findById(id)
            ?: throw EntityNotFoundException("Did not find place")
        }
      }.memoize()

       */
      logger.info(
        "Request with parameters: ${fileList.get(index).originalFilename}  ${isGoodPictureList.get(index)}, ${motiveIdList.get(index)}" +
          "PlaceId: $placeIdList"
      )

      val place = placeRepository
        .findById(placeIdList.get(index))
        ?: throw EntityNotFoundException("Did not find place")

      val securityLevelDto: SecurityLevelDto = securityLevelRepository
        .findById(securityLevelIdList.get(index))
        ?: throw EntityNotFoundException("Did not find securitulevel")

      val motive = motiveRepository
        .findById(motiveIdList.get(index))
        ?: throw EntityNotFoundException("Did not find motive")

      val gang: GangDto = gangRepository
        .findById(gangIdList.get(index))
        ?: throw EntityNotFoundException("Did not find gang")

      val photoGangBanger = photoGangBangerRepository
        .findById(photoGangBangerIdList.get(index))
        ?: throw EntityNotFoundException("Did not find photoGangBanger")

      val album = albumRepository
        .findById(albumIdList.get(index))
        ?: throw EntityNotFoundException("Did not find album")
      val category = categoryRepository
        .findById(categoryIdList.get(index))
        ?: throw EntityNotFoundException("Did not find category")

      val validatedFileName = ImageFileName(file.originalFilename ?: "")

      // Generate Objects
      val (photoDto, imageFileName) = PhotoDto.createWithFileName(
        fileName = validatedFileName,
        isGoodPicture = isGoodPictureList.get(index),
        motive = motive,
        placeDto = place,
        securityLevel = securityLevelDto,
        gang = gang,
        albumDto = album,
        categoryDto = category,
        photoGangBangerDto = photoGangBanger,
        photoTags = listOf() // TODO: Pass in photoTags
      )

      val filePath = generateFilePath(
        // TODO: Rename to fileName
        fileName = ImageFileName(photoDto.largeUrl),
        securityLevel = photoDto.securityLevel,
        // TODO: Fix this
        size = PhotoSize.Large,
        motive = motive
      )
      // Save file to disk
      try {
        logger.info("Saving file to disk $filePath")
        Files.copy(file.inputStream, filePath).toString()
      } catch (ex: IOException) {
        logger.error(filePath.toString())
        logger.error(filePath.toAbsolutePath().toString())
        logger.error(ex.toString())
        throw IOException("Something went wrong when saving image file to disk")
      }
      // Add PhotoModel to Database
      try {
        photoRepository
          .createPhoto(photoDto)
        logger.info("Photo created: ${photoDto.largeUrl} ")
      } catch (ex: Exception) {
        logger.error("Failed to save Photo to Database. Deleting file")
        Files.delete(filePath)
      }

      photoDto.largeUrl // Return URLs
    }
  }

  override fun saveAnalogPhotos(
    isGoodPictureList: List<Boolean>,
    motiveIdList: List<UUID>,
    placeIdList: List<UUID>,
    securityLevelIdList: List<UUID>,
    gangIdList: List<UUID>,
    photoGangBangerIdList: List<UUID>,
    fileList: List<MultipartFile>
  ): List<String> {
    TODO("Not implemented yet")
  }

  override fun createNewMotiveAndSaveDigitalPhotos(
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
  ): List<String> {
    val isValidRequest = photoFileList.size > 0 && (
      photoFileList.size == isGoodPhotoList.size &&
        photoFileList.size == tagList.size
      )
    logger.warn("Is valid request? $isValidRequest")
    if (!isValidRequest) throw java.lang.IllegalArgumentException("photoFileList, isGoodPhotoList and tagList are of unequal length or not given")
    logger.info("createNewMotiveAndSaveDigitalPhotos() $tagList")
    val eventOwnerDto = eventOwnerRepository
      .findByEventOwnerName(EventOwnerName.valueOf(eventOwnerString))
      ?: throw EntityNotFoundException("Did not find eventOwner")

    val albumDto = albumRepository
      .findById(albumId)
      ?: throw EntityNotFoundException("Did not find album")

    val photoGangBangerDto = photoGangBangerRepository
      .findById(photoGangBangerId)
      ?: throw EntityNotFoundException("Did not find photoGangBanger")

    val securityLevelDto = securityLevelRepository
      .findById(securityLevelId)
      ?: throw EntityNotFoundException("SecurityLevelNotFound")

    val categoryDto = categoryRepository
      .findByName(categoryName)
      // Should we use categoryId instead of categoryName??
      ?: throw EntityNotFoundException("Did not find category")

    // Fetch object from database, if not exist create object
    // and save to database
    // TODO: Wait with saving place to database to later?
    val placeDto = fetchOrCreatePlaceDto(placeString)

    val motiveDto = fetchOrCreateMotive(
      motiveTitle = motiveString,
      eventOwnerDto = eventOwnerDto,
      categoryDto = categoryDto,
      albumDto = albumDto
    )

    val numPhotoGenerated = photoFileList.mapIndexed { index, photoFile ->
      val photoTagDtos = tagList.get(index).map {
        photoTagRepository
          .findByName(it)
          ?: PhotoTagDto(name = it)
            .apply {
              photoTagRepository
                .create(
                  this
                )
            }
      }
      val isGoodPhoto = isGoodPhotoList.get(index)

      val (photoDto, imageFileName) = PhotoDto.createWithFileName(
        securityLevel = securityLevelDto,
        placeDto = placeDto,
        motive = motiveDto,
        isGoodPicture = isGoodPhoto,
        photoGangBangerDto = photoGangBangerDto,
        albumDto = albumDto,
        photoTags = photoTagDtos,
        categoryDto = categoryDto,
        fileName = ImageFileName(
          photoFile.originalFilename ?: ""
        )
      )

      // Generate PhotoDto
      photoRepository
        .createPhoto(photoDto)

      // GeneratePaths
      // TODO: Do not hard code this. Fetch from application
      "http://localhost:8383/${store(photoFile, photoDto, imageFileName)}"
      // Save shit
    }

    return numPhotoGenerated
  }
  fun fetchOrCreatePlaceDto(placeName: String) = placeRepository
    .findByName(placeName)
    ?: PlaceDto(name = placeName)
      .also {
        placeRepository
          .create(
            it
          )
      }

  fun fetchOrCreateMotive(
    motiveTitle: String,
    categoryDto: CategoryDto,
    eventOwnerDto: EventOwnerDto,
    albumDto: AlbumDto
  ): MotiveDto =
    motiveRepository
      .findByTitle(motiveTitle)
      ?.toDto()
      ?: motiveRepository.create(
        MotiveDto(
          title = motiveTitle,
          categoryDto = categoryDto,
          eventOwnerDto = eventOwnerDto,
          albumDto = albumDto
        )
      )

  override fun getCarouselPhotos(): List<PhotoDto> = photoRepository
    .findCarouselPhotos()

  override fun getAllAnalogPhotos(): List<PhotoDto> = photoRepository
    .findAllAnalogPhotos()

  override fun getAllDigitalPhotos(): List<PhotoDto> = photoRepository
    .findAllDigitalPhotos()

  fun getByMotiveId(id: UUID): List<PhotoDto>? = photoRepository.findByMotiveId(id)

  override fun getById(id: UUID): PhotoDto = photoRepository?.findById(id) ?: throw EntityNotFoundException("Did not find photo")

  override fun getAll(): List<PhotoDto> {
    return photoRepository
      .findAll()
  }

  fun test(): Any {
    return photoRepository.findAll()
  }
}

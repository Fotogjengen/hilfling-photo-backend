package no.fg.hilflingbackend.service

import no.fg.hilflingbackend.configurations.ImageFileStorageProperties
import no.fg.hilflingbackend.dto.GangDto
import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.dto.SecurityLevelDto
import no.fg.hilflingbackend.dto.toDto
import no.fg.hilflingbackend.model.SecurityLevel
import no.fg.hilflingbackend.repository.GangRepository
import no.fg.hilflingbackend.repository.MotiveRepository
import no.fg.hilflingbackend.repository.PhotoGangBangerRepository
import no.fg.hilflingbackend.repository.PhotoRepository
import no.fg.hilflingbackend.repository.PlaceRepository
import no.fg.hilflingbackend.repository.SecurityLevelRepository
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
  val motiveRepository: MotiveRepository,
  val placeRepository: PlaceRepository,
  val securityLevelRepository: SecurityLevelRepository,
  val photoGangBangerRepository: PhotoGangBangerRepository,
) : IPhotoService {

  val logger = LoggerFactory.getLogger(this::class.java)
  val profileLocation = Paths.get("filestorage/PROFILE")

  val rootLocation = Paths.get("static-files/static/img/")
  val photoGangBangerLocation = Paths.get("static-files/static/img//FG")
  val houseMemberLocation = Paths.get("static-files/static/img//HUSFOLK")
  val allLocation = Paths.get("static-files/static/img//ALLE")

  fun savePhotosToDisk() {
    throw NotImplementedError()
  }

  /**
   * FilePath is generated as follows:
   * /basePath/securitylevel/fileSize-+filename.fileExtension
   */
  fun generateFilePath(
    fileName: ImageFileName,
    securityLevel: SecurityLevelDto,
    size: PhotoSize,
  ): Path {
    // BasePath
    val basePath = imageFileStorageProperties.savedPhotosPath
    println("BaseBath from config: $basePath")
    val fullFilePath = Paths.get("$basePath/${securityLevel.securityLevelType}/$size-${fileName.filename}")
    println(fullFilePath)
    // TODO: Check if directories exiist before continou
    // Files.isDirectory()
    return fullFilePath
  }

  fun store(file: MultipartFile, securityLevelType: SecurityLevelType): String {
    val newFileName = "${UUID.randomUUID()}.${file.originalFilename!!.split('.').get(1)}"
    val location = when (securityLevelType) {
      SecurityLevelType.FG -> this.photoGangBangerLocation.resolve(newFileName)
      SecurityLevelType.HUSFOLK -> this.houseMemberLocation.resolve(newFileName)
      SecurityLevelType.ALLE -> this.allLocation.resolve(newFileName)
      SecurityLevelType.PROFILE -> this.profileLocation.resolve(newFileName)
      else -> throw IllegalArgumentException("Invalid security level")
    }
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
    if(!Files.exists(profileLocation)) {
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
        ?.toDto()
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

      val validatedFileName = ImageFileName(file.originalFilename ?: "")

      // Generate Objects
      val photoDto = PhotoDto.createWithFileName(
        fileName = validatedFileName,
        isGoodPicture = isGoodPictureList.get(index),
        motive = motive,
        placeDto = place,
        securityLevel = securityLevelDto,
        gang = gang,
        photoGangBangerDto = photoGangBanger
      )
      val filePath = generateFilePath(
        // TODO: Rename to fileName
        fileName = ImageFileName(photoDto.largeUrl),
        securityLevel = photoDto.securityLevel,
        // TODO: Fix this
        size = PhotoSize.Large
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
          .createPhoto(photoDto.toEntity())
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

  override fun getCarouselPhotos(): List<PhotoDto> = photoRepository
    .findCarouselPhotos()

  override fun getAllAnalogPhotos(): List<PhotoDto> = photoRepository
    .findAllAnalogPhotos()

  override fun getAllDigitalPhotos(): List<PhotoDto> = photoRepository
    .findAllDigitalPhotos()

  override fun getById(id: UUID): PhotoDto = photoRepository
    ?.findById(id)
    ?: throw EntityNotFoundException("Did not find photo")

  override fun getAll(): List<PhotoDto> = photoRepository
    .findAll()
}

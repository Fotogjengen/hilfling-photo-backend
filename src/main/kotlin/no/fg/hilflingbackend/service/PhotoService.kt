package no.fg.hilflingbackend.service

import no.fg.hilflingbackend.model.SecurityLevel
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.UUID
import java.util.stream.Stream

@Service
class PhotoService {
  @Autowired
  lateinit var environment: Environment

  val log = LoggerFactory.getLogger(this::class.java)

  val rootLocation = Paths.get("static-files/static/img/")
  val photoGangBangerLocation = Paths.get("static-files/static/img//FG")
  val houseMemberLocation = Paths.get("static-files/static/img//HUSFOLK")
  val allLocation = Paths.get("static-files/static/img//ALLE")

  fun store(file: MultipartFile, securityLevel: SecurityLevel): String {
    val location: Path
    val newFileName = "${UUID.randomUUID()}.${file.originalFilename!!.split('.').get(1)}"
    location = when (securityLevel.type) {
      "FG" -> this.photoGangBangerLocation.resolve(newFileName)
      "HUSFOLK" -> this.houseMemberLocation.resolve(newFileName)
      "ALLE" -> this.allLocation.resolve(newFileName)
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
    log.info("Deletes and recreates img static files location")
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
  }

  fun loadFiles(): Stream<Path> {
    return Files.walk(this.rootLocation, 1)
      .filter { path -> path != this.rootLocation }
      .map(this.rootLocation::relativize)
  }
}

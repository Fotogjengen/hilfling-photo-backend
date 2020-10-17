package no.fg.hilflingbackend.service

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import no.fg.hilflingbackend.model.SecurityLevel
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile
import java.lang.RuntimeException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream

@Service
class PhotoService {
    val log = LoggerFactory.getLogger(this::class.java)

    val rootLocation = Paths.get("filestorage")
    val photoGangBangerLocation = Paths.get("filestorage/FG")
    val houseMemberLocation = Paths.get("filestorage/HUSFOLK")
    val allLocation = Paths.get("filestorage/ALLE")

    fun store(file: MultipartFile, securityLevel: SecurityLevel): String {
        val location: Path
        when(securityLevel.type) {
            "FG" -> location = this.photoGangBangerLocation.resolve(file.originalFilename)
            "HUSFOLK" -> location = this.houseMemberLocation.resolve(file.originalFilename)
            "ALLE" -> location = this.allLocation.resolve(file.originalFilename)
            else -> throw IllegalArgumentException("Invalid security level")
        }
        return Files.copy(file.inputStream, location).toString()
    }

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
                .filter{path -> path != this.rootLocation }
                .map(this.rootLocation::relativize)
    }

}
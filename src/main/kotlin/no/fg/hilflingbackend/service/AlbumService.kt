package no.fg.hilflingbackend.service

import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths

@Service
class AlbumService {
  fun createDirectory(albumName: String): String {
    val location = Paths.get("static-files/static/img/".plus(albumName))
    if (!Files.exists(location)) {
      Files.createDirectory(location)
    }
    return location.toString();
  }
}

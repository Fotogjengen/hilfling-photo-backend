package no.fg.hilflingbackend.service

import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths

@Service
class SearchService {
  fun findAlbums(searchTerm: String): String {
    val location = Paths.get("static-files/static/img/".plus(albumName))
    if (!Files.exists(location)) {
      Files.createDirectory(location)
    }
    return location.toString()
  }
}

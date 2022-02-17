package no.fg.hilflingbackend.blobStorage

import org.springframework.web.multipart.MultipartFile
import java.io.File

interface IBlobStorage {
  fun saveFile(file: MultipartFile): String
}

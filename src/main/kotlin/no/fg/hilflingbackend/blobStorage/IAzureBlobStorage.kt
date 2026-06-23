package no.fg.hilflingbackend.blobStorage

import no.fg.hilflingbackend.valueobject.ImageFileName
import org.springframework.web.multipart.MultipartFile

interface IAzureBlobStorage {
  fun saveFile(
    file: MultipartFile,
    blobContainerName: String = "alle",
    fileName: ImageFileName,
  ): String
}

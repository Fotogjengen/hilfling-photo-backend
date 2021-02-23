package no.fg.hilflingbackend.utils

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

fun convertToValidFolderName(folderNameString: String): String {
  return folderNameString
    .toLowerCase()
    .replace("æ", "ae", ignoreCase = true)
    .replace("ø", "oe", ignoreCase = true)
    .replace("å", "aa", ignoreCase = true)
    .replace(" ", "_", ignoreCase = true)
}

fun createFolder(basePathString: String, folderName: String): Path {
  val fullPath = Paths.get(basePathString + folderName)
  if (Files.isDirectory(fullPath)) throw IOException("Folder already exists")

  return Files.createDirectories(fullPath)
}

package no.fg.hilflingbackend.service

class OsPathService {
  fun convertToValidFolderName(folderNameString: String): Any? {
    return folderNameString
      .toLowerCase()
      .replace("æ", "ae", ignoreCase = true)
      .replace("ø", "oe", ignoreCase = true)
      .replace("å", "aa", ignoreCase = true)
      .replace(" ", "_", ignoreCase = true)
  }
}

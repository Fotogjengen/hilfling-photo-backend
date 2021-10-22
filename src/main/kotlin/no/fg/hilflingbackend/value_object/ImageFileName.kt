package no.fg.hilflingbackend.value_object

data class ImageFileName private constructor(val filename: String) {
  companion object {
    operator fun invoke(filename: String): ImageFileName {
      return if (!isValidImageFileName(filename)) throw IllegalArgumentException("Not valid filename")
      else ImageFileName(filename)
    }
    private fun isValidImageFileName(filename: String): Boolean {
      println(filename)
      // Must have a valid file extension
      return filename.split(".").size >= 2 &&
        (
          filename.contains(".jpg", ignoreCase = true) ||
            filename.contains(".png", ignoreCase = true)
          )
    }
  }
  // Example: .jpg, .png
  fun getFileExtension() =
    ".${this.filename.split(".").last()}"
}

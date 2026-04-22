package no.fg.hilflingbackend.value_object

import no.fg.hilflingbackend.utils.sanitizeFileName

data class ImageFileName private constructor(
  val filename: String,
) {
  companion object {
    operator fun invoke(filename: String): ImageFileName {
      val sanitized = sanitizeFileName(filename)
      if (!isValidImageFileName(sanitized)) {
        throw IllegalArgumentException("Not valid filename: $sanitized")
      }
      return ImageFileName(sanitized)
    }

    private fun isValidImageFileName(filename: String): Boolean {
      // Must have a valid file extension
      return filename.split(".").size >= 2 &&
        (
          filename.contains(".jpg", ignoreCase = true) ||
            filename.contains(".png", ignoreCase = true)
        )
    }
  }

  // Example: .jpg, .png
  fun getFileExtension() = ".${this.filename.split(".").last()}"
}

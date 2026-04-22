package no.fg.hilflingbackend.utils

private val JPEG_MAGIC = byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte())
private val PNG_MAGIC = byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47)

fun validateImageMagicBytes(fileBytes: ByteArray) {
  require(fileBytes.size >= 4) {
    "File content does not match a supported image format (JPEG or PNG)"
  }
  val isJpeg = fileBytes.take(3).toByteArray().contentEquals(JPEG_MAGIC)
  val isPng = fileBytes.take(4).toByteArray().contentEquals(PNG_MAGIC)
  require(isJpeg || isPng) {
    "File content does not match a supported image format (JPEG or PNG)"
  }
}

fun validateContentType(contentType: String?) {
  require(contentType != null && contentType in listOf("image/jpeg", "image/png")) {
    "Unsupported content type: $contentType. Only image/jpeg and image/png are allowed."
  }
}

fun sanitizeFileName(fileName: String): String =
  fileName
    .replace("..", "")
    .replace("\\", "/")
    .substringAfterLast("/")

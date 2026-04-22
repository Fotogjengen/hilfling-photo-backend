package no.fg.hilflingbackend.utils

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class FileValidationUtilsTest {
  @Test
  fun `validateImageMagicBytes accepts valid JPEG`() {
    val jpegBytes = byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte(), 0xE0.toByte())
    assertDoesNotThrow { validateImageMagicBytes(jpegBytes) }
  }

  @Test
  fun `validateImageMagicBytes accepts valid PNG`() {
    val pngBytes = byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A)
    assertDoesNotThrow { validateImageMagicBytes(pngBytes) }
  }

  @Test
  fun `validateImageMagicBytes rejects non-image file`() {
    val pdfBytes = byteArrayOf(0x25, 0x50, 0x44, 0x46)
    val ex = assertThrows(IllegalArgumentException::class.java) {
      validateImageMagicBytes(pdfBytes)
    }
    assertEquals("File content does not match a supported image format (JPEG or PNG)", ex.message)
  }

  @Test
  fun `validateImageMagicBytes rejects empty file`() {
    assertThrows(IllegalArgumentException::class.java) {
      validateImageMagicBytes(byteArrayOf())
    }
  }

  @Test
  fun `validateContentType accepts image jpeg`() {
    assertDoesNotThrow { validateContentType("image/jpeg") }
  }

  @Test
  fun `validateContentType accepts image png`() {
    assertDoesNotThrow { validateContentType("image/png") }
  }

  @Test
  fun `validateContentType rejects application pdf`() {
    assertThrows(IllegalArgumentException::class.java) {
      validateContentType("application/pdf")
    }
  }

  @Test
  fun `validateContentType rejects null`() {
    assertThrows(IllegalArgumentException::class.java) {
      validateContentType(null)
    }
  }

  @Test
  fun `sanitizeFileName strips path traversal sequences`() {
    assertEquals("malicious.jpg", sanitizeFileName("../../malicious.jpg"))
  }

  @Test
  fun `sanitizeFileName strips backslash path traversal`() {
    assertEquals("malicious.jpg", sanitizeFileName("..\\..\\malicious.jpg"))
  }

  @Test
  fun `sanitizeFileName preserves normal filename`() {
    assertEquals("photo_2024.jpg", sanitizeFileName("photo_2024.jpg"))
  }

  @Test
  fun `sanitizeFileName strips directory components`() {
    assertEquals("file.png", sanitizeFileName("some/path/file.png"))
  }
}

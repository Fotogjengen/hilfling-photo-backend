# Sub-project 1: Critical Code Fixes — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Fix SQL injection, upgrade encryption from AES-CBC to AES-GCM, gate the seed endpoint behind a dev profile, and harden file upload validation.

**Architecture:** Four independent fixes touching search repositories, encryption utilities, the seed controller, and file upload validation. Each fix is self-contained and testable in isolation.

**Tech Stack:** Kotlin, Spring Boot 4, Ktorm, JCE (AES-GCM), JUnit 5

---

## File Structure

| Action | Path | Responsibility |
|--------|------|----------------|
| Create | `src/main/kotlin/no/fg/hilflingbackend/utils/SqlUtils.kt` | LIKE-pattern escaping utility |
| Modify | `src/main/kotlin/no/fg/hilflingbackend/repository/SearchRepository.kt` | Use escaped search term |
| Modify | `src/main/kotlin/no/fg/hilflingbackend/repository/SearchSuggestionsRepository.kt` | Use escaped search term |
| Modify | `src/main/kotlin/no/fg/hilflingbackend/repository/EventCardRepository.kt` | Use escaped search term |
| Modify | `src/main/kotlin/no/fg/hilflingbackend/utils/EncryptionUtils.kt` | Migrate to AES-GCM |
| Modify | `src/main/kotlin/no/fg/hilflingbackend/configurations/SecurityConfig.kt` | Fix key validation, fail fast |
| Modify | `src/main/kotlin/no/fg/hilflingbackend/controller/SeedController.kt` | Add @Profile annotation |
| Create | `src/main/kotlin/no/fg/hilflingbackend/utils/FileValidationUtils.kt` | Magic byte + content-type validation |
| Modify | `src/main/kotlin/no/fg/hilflingbackend/value_object/ImageFileName.kt` | Add path traversal sanitization |
| Modify | `src/main/kotlin/no/fg/hilflingbackend/service/PhotoService.kt` | Call file validation before storing |
| Create | `src/test/kotlin/no/fg/hilflingbackend/utils/SqlUtilsTest.kt` | Tests for LIKE escaping |
| Create | `src/test/kotlin/no/fg/hilflingbackend/utils/EncryptionUtilsTest.kt` | Tests for AES-GCM encrypt/decrypt |
| Create | `src/test/kotlin/no/fg/hilflingbackend/utils/FileValidationUtilsTest.kt` | Tests for magic byte validation |

---

### Task 1: SQL Injection Fix — LIKE Escape Utility

**Files:**
- Create: `src/main/kotlin/no/fg/hilflingbackend/utils/SqlUtils.kt`
- Create: `src/test/kotlin/no/fg/hilflingbackend/utils/SqlUtilsTest.kt`

- [ ] **Step 1: Write the failing test**

Create `src/test/kotlin/no/fg/hilflingbackend/utils/SqlUtilsTest.kt`:

```kotlin
package no.fg.hilflingbackend.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SqlUtilsTest {

  @Test
  fun `escapeLikePattern escapes percent signs`() {
    assertEquals("100\\% done", escapeLikePattern("100% done"))
  }

  @Test
  fun `escapeLikePattern escapes underscores`() {
    assertEquals("file\\_name", escapeLikePattern("file_name"))
  }

  @Test
  fun `escapeLikePattern escapes backslashes`() {
    assertEquals("path\\\\to", escapeLikePattern("path\\to"))
  }

  @Test
  fun `escapeLikePattern handles combined special characters`() {
    assertEquals("a\\%b\\_c\\\\d", escapeLikePattern("a%b_c\\d"))
  }

  @Test
  fun `escapeLikePattern leaves normal text unchanged`() {
    assertEquals("hello world", escapeLikePattern("hello world"))
  }

  @Test
  fun `escapeLikePattern handles empty string`() {
    assertEquals("", escapeLikePattern(""))
  }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn test -pl . -Dtest="no.fg.hilflingbackend.utils.SqlUtilsTest" -Dsurefire.failIfNoSpecifiedTests=false`
Expected: Compilation failure — `escapeLikePattern` does not exist yet.

- [ ] **Step 3: Write the implementation**

Create `src/main/kotlin/no/fg/hilflingbackend/utils/SqlUtils.kt`:

```kotlin
package no.fg.hilflingbackend.utils

/**
 * Escapes special characters in a string intended
 * for use inside a SQL LIKE/ILIKE pattern.
 * Escapes: \ → \\, % → \%, _ → \_
 */
fun escapeLikePattern(input: String): String =
  input
    .replace("\\", "\\\\")
    .replace("%", "\\%")
    .replace("_", "\\_")
```

- [ ] **Step 4: Run test to verify it passes**

Run: `mvn test -pl . -Dtest="no.fg.hilflingbackend.utils.SqlUtilsTest"`
Expected: All 6 tests PASS.

- [ ] **Step 5: Commit**

```bash
git add src/main/kotlin/no/fg/hilflingbackend/utils/SqlUtils.kt src/test/kotlin/no/fg/hilflingbackend/utils/SqlUtilsTest.kt
git commit -m "feat: add LIKE-pattern escape utility to prevent SQL injection"
```

---

### Task 2: SQL Injection Fix — Apply Escaping to Repositories

**Files:**
- Modify: `src/main/kotlin/no/fg/hilflingbackend/repository/SearchRepository.kt`
- Modify: `src/main/kotlin/no/fg/hilflingbackend/repository/SearchSuggestionsRepository.kt`
- Modify: `src/main/kotlin/no/fg/hilflingbackend/repository/EventCardRepository.kt`

- [ ] **Step 1: Update SearchRepository.kt**

Change line 23 from:
```kotlin
it.title ilike "%$searchTerm%"
```
to:
```kotlin
it.title ilike "%${escapeLikePattern(searchTerm)}%"
```

Add import at the top:
```kotlin
import no.fg.hilflingbackend.utils.escapeLikePattern
```

Full file after change:

```kotlin
package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.toList
import me.liuwj.ktorm.support.postgresql.ilike
import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.model.motives
import no.fg.hilflingbackend.model.toDto
import no.fg.hilflingbackend.utils.escapeLikePattern
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
open class SearchRepository {

  @Autowired
  open lateinit var database: Database

  fun findBySearchTerm(searchTerm: String): List<MotiveDto> =
    database
      .motives
      .filter {
        it.title ilike "%${escapeLikePattern(searchTerm)}%"
      }.toList()
      .map { it.toDto() }
}
```

- [ ] **Step 2: Update SearchSuggestionsRepository.kt**

Change line 28 from:
```kotlin
database.motives.filter { it.title ilike "%$searchTerm%" }.take(10).toList().map {
```
to:
```kotlin
database.motives.filter { it.title ilike "%${escapeLikePattern(searchTerm)}%" }.take(10).toList().map {
```

Add import:
```kotlin
import no.fg.hilflingbackend.utils.escapeLikePattern
```

- [ ] **Step 3: Update EventCardRepository.kt**

Change line 89 from:
```kotlin
Motives.title ilike "%$searchTerm%"
```
to:
```kotlin
Motives.title ilike "%${escapeLikePattern(searchTerm)}%"
```

Add import:
```kotlin
import no.fg.hilflingbackend.utils.escapeLikePattern
```

- [ ] **Step 4: Verify compilation**

Run: `mvn compile`
Expected: BUILD SUCCESS

- [ ] **Step 5: Commit**

```bash
git add src/main/kotlin/no/fg/hilflingbackend/repository/SearchRepository.kt src/main/kotlin/no/fg/hilflingbackend/repository/SearchSuggestionsRepository.kt src/main/kotlin/no/fg/hilflingbackend/repository/EventCardRepository.kt
git commit -m "fix: escape LIKE patterns in search repositories to prevent SQL injection"
```

---

### Task 3: Encryption Upgrade — AES-GCM

**Files:**
- Modify: `src/main/kotlin/no/fg/hilflingbackend/utils/EncryptionUtils.kt`
- Create: `src/test/kotlin/no/fg/hilflingbackend/utils/EncryptionUtilsTest.kt`

- [ ] **Step 1: Write the failing test**

Create `src/test/kotlin/no/fg/hilflingbackend/utils/EncryptionUtilsTest.kt`:

```kotlin
package no.fg.hilflingbackend.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import javax.crypto.spec.SecretKeySpec

class EncryptionUtilsTest {

  private val testKey = SecretKeySpec(ByteArray(16) { it.toByte() }, "AES")

  @Test
  fun `encrypt and decrypt round-trip produces original text`() {
    val plaintext = "http://localhost:8383/static/img/FG/photo.jpg"
    val encrypted = EncryptionUtils.encrypt(plaintext, testKey)
    val decrypted = EncryptionUtils.decrypt(encrypted, testKey)
    assertEquals(plaintext, decrypted)
  }

  @Test
  fun `encrypt produces different ciphertext each time due to random IV`() {
    val plaintext = "same input"
    val encrypted1 = EncryptionUtils.encrypt(plaintext, testKey)
    val encrypted2 = EncryptionUtils.encrypt(plaintext, testKey)
    assertNotEquals(encrypted1, encrypted2)
  }

  @Test
  fun `decrypt with wrong key throws exception`() {
    val plaintext = "secret data"
    val encrypted = EncryptionUtils.encrypt(plaintext, testKey)
    val wrongKey = SecretKeySpec(ByteArray(16) { (it + 1).toByte() }, "AES")
    assertThrows(Exception::class.java) {
      EncryptionUtils.decrypt(encrypted, wrongKey)
    }
  }

  @Test
  fun `decrypt tampered ciphertext throws exception`() {
    val plaintext = "important data"
    val encrypted = EncryptionUtils.encrypt(plaintext, testKey)
    // Tamper with the ciphertext by flipping the last character
    val tampered = encrypted.dropLast(1) + if (encrypted.last() == 'A') 'B' else 'A'
    assertThrows(Exception::class.java) {
      EncryptionUtils.decrypt(tampered, testKey)
    }
  }

  @Test
  fun `encrypt and decrypt handles empty string`() {
    val plaintext = ""
    val encrypted = EncryptionUtils.encrypt(plaintext, testKey)
    val decrypted = EncryptionUtils.decrypt(encrypted, testKey)
    assertEquals(plaintext, decrypted)
  }

  @Test
  fun `encrypt and decrypt handles unicode`() {
    val plaintext = "photo-\u00e6\u00f8\u00e5-test"
    val encrypted = EncryptionUtils.encrypt(plaintext, testKey)
    val decrypted = EncryptionUtils.decrypt(encrypted, testKey)
    assertEquals(plaintext, decrypted)
  }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn test -pl . -Dtest="no.fg.hilflingbackend.utils.EncryptionUtilsTest"`
Expected: Some tests fail — the tamper-detection test will fail because AES-CBC doesn't detect tampering.

- [ ] **Step 3: Rewrite EncryptionUtils to use AES-GCM**

Replace the entire content of `src/main/kotlin/no/fg/hilflingbackend/utils/EncryptionUtils.kt`:

```kotlin
package no.fg.hilflingbackend.utils

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object EncryptionUtils {
  private const val ALGORITHM = "AES/GCM/NoPadding"
  private const val GCM_IV_LENGTH = 12
  private const val GCM_TAG_LENGTH = 128

  fun encrypt(
    data: String,
    secretKey: SecretKey,
  ): String {
    val iv = ByteArray(GCM_IV_LENGTH)
    java.security.SecureRandom().nextBytes(iv)
    val gcmSpec = GCMParameterSpec(GCM_TAG_LENGTH, iv)

    val cipher = Cipher.getInstance(ALGORITHM)
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec)

    val encryptedBytes = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
    val encryptedWithIv = iv + encryptedBytes

    return Base64.getEncoder().encodeToString(encryptedWithIv)
  }

  fun decrypt(
    encryptedData: String,
    secretKey: SecretKey,
  ): String {
    val decodedBytes = Base64.getDecoder().decode(encryptedData)

    val iv = decodedBytes.copyOfRange(0, GCM_IV_LENGTH)
    val encryptedBytes = decodedBytes.copyOfRange(GCM_IV_LENGTH, decodedBytes.size)
    val gcmSpec = GCMParameterSpec(GCM_TAG_LENGTH, iv)

    val cipher = Cipher.getInstance(ALGORITHM)
    cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec)

    val decryptedBytes = cipher.doFinal(encryptedBytes)
    return String(decryptedBytes, Charsets.UTF_8)
  }
}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `mvn test -pl . -Dtest="no.fg.hilflingbackend.utils.EncryptionUtilsTest"`
Expected: All 6 tests PASS (including tamper detection).

- [ ] **Step 5: Commit**

```bash
git add src/main/kotlin/no/fg/hilflingbackend/utils/EncryptionUtils.kt src/test/kotlin/no/fg/hilflingbackend/utils/EncryptionUtilsTest.kt
git commit -m "feat: upgrade encryption from AES-CBC to AES-GCM for authenticated encryption"
```

---

### Task 4: Fix Encryption Key Management

**Files:**
- Modify: `src/main/kotlin/no/fg/hilflingbackend/configurations/SecurityConfig.kt`

- [ ] **Step 1: Rewrite SecurityConfig.kt**

Replace the entire content of `src/main/kotlin/no/fg/hilflingbackend/configurations/SecurityConfig.kt`:

```kotlin
package no.fg.hilflingbackend.configurations

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Configuration
class SecurityConfig {
  @Bean
  fun secretKey(): SecretKey {
    val key = System.getenv("ENCRYPTION_KEY")
      ?: throw IllegalStateException(
        "ENCRYPTION_KEY environment variable is required. " +
          "Must be 16, 24, or 32 bytes long for AES-128, AES-192, or AES-256."
      )

    val keyBytes = key.toByteArray(Charsets.UTF_8)
    require(keyBytes.size == 16 || keyBytes.size == 24 || keyBytes.size == 32) {
      "Invalid key length: ENCRYPTION_KEY must be exactly 16, 24, or 32 bytes " +
        "(got ${keyBytes.size} bytes)."
    }
    return SecretKeySpec(keyBytes, "AES")
  }
}
```

- [ ] **Step 2: Verify compilation**

Run: `mvn compile`
Expected: BUILD SUCCESS

- [ ] **Step 3: Commit**

```bash
git add src/main/kotlin/no/fg/hilflingbackend/configurations/SecurityConfig.kt
git commit -m "fix: require ENCRYPTION_KEY env var and validate byte length, not string length"
```

---

### Task 5: Gate Seed Endpoint Behind Dev Profile

**Files:**
- Modify: `src/main/kotlin/no/fg/hilflingbackend/controller/SeedController.kt`

- [ ] **Step 1: Add @Profile annotation**

Replace the entire content of `src/main/kotlin/no/fg/hilflingbackend/controller/SeedController.kt`:

```kotlin
package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.MockDataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Profile("dev", "test")
@RestController
@RequestMapping("/seed")
class SeedController {
  @Autowired
  lateinit var mockDataService: MockDataService

  @GetMapping
  fun seedMockdata(): String {
    mockDataService
      .seedMockData()
    return "Mock data seeded"
  }
}
```

- [ ] **Step 2: Verify compilation**

Run: `mvn compile`
Expected: BUILD SUCCESS

- [ ] **Step 3: Commit**

```bash
git add src/main/kotlin/no/fg/hilflingbackend/controller/SeedController.kt
git commit -m "fix: gate /seed endpoint behind dev and test Spring profiles"
```

---

### Task 6: File Upload Hardening — Validation Utility

**Files:**
- Create: `src/main/kotlin/no/fg/hilflingbackend/utils/FileValidationUtils.kt`
- Create: `src/test/kotlin/no/fg/hilflingbackend/utils/FileValidationUtilsTest.kt`

- [ ] **Step 1: Write the failing test**

Create `src/test/kotlin/no/fg/hilflingbackend/utils/FileValidationUtilsTest.kt`:

```kotlin
package no.fg.hilflingbackend.utils

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class FileValidationUtilsTest {

  @Test
  fun `validateImageMagicBytes accepts valid JPEG`() {
    // JPEG magic bytes: FF D8 FF
    val jpegBytes = byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte(), 0xE0.toByte())
    assertDoesNotThrow { validateImageMagicBytes(jpegBytes) }
  }

  @Test
  fun `validateImageMagicBytes accepts valid PNG`() {
    // PNG magic bytes: 89 50 4E 47
    val pngBytes = byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A)
    assertDoesNotThrow { validateImageMagicBytes(pngBytes) }
  }

  @Test
  fun `validateImageMagicBytes rejects non-image file`() {
    // PDF magic bytes: 25 50 44 46
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
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn test -pl . -Dtest="no.fg.hilflingbackend.utils.FileValidationUtilsTest" -Dsurefire.failIfNoSpecifiedTests=false`
Expected: Compilation failure — functions don't exist.

- [ ] **Step 3: Write the implementation**

Create `src/main/kotlin/no/fg/hilflingbackend/utils/FileValidationUtils.kt`:

```kotlin
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
```

- [ ] **Step 4: Run test to verify it passes**

Run: `mvn test -pl . -Dtest="no.fg.hilflingbackend.utils.FileValidationUtilsTest"`
Expected: All 12 tests PASS.

- [ ] **Step 5: Commit**

```bash
git add src/main/kotlin/no/fg/hilflingbackend/utils/FileValidationUtils.kt src/test/kotlin/no/fg/hilflingbackend/utils/FileValidationUtilsTest.kt
git commit -m "feat: add file upload validation for magic bytes, content type, and filename sanitization"
```

---

### Task 7: File Upload Hardening — Integrate Validation into PhotoService

**Files:**
- Modify: `src/main/kotlin/no/fg/hilflingbackend/value_object/ImageFileName.kt`
- Modify: `src/main/kotlin/no/fg/hilflingbackend/service/PhotoService.kt`

- [ ] **Step 1: Update ImageFileName to sanitize filenames**

Replace the content of `src/main/kotlin/no/fg/hilflingbackend/value_object/ImageFileName.kt`:

```kotlin
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
      return filename.split(".").size >= 2 &&
        (
          filename.contains(".jpg", ignoreCase = true) ||
            filename.contains(".png", ignoreCase = true)
        )
    }
  }

  fun getFileExtension() = ".${this.filename.split(".").last()}"
}
```

- [ ] **Step 2: Add file validation calls to PhotoService**

In `src/main/kotlin/no/fg/hilflingbackend/service/PhotoService.kt`, add imports at the top:

```kotlin
import no.fg.hilflingbackend.utils.validateContentType
import no.fg.hilflingbackend.utils.validateImageMagicBytes
```

In the `saveDigitalPhotos` method, add validation before `ImageFileName` construction (before the existing line 234 `val validatedFileName = ImageFileName(file.originalFilename ?: "")`):

```kotlin
      // Validate file content
      validateContentType(file.contentType)
      validateImageMagicBytes(file.bytes)
```

In the `createNewMotiveAndSaveDigitalPhotos` method, add the same validation inside the `photoFileList.mapIndexed` block, before the `ImageFileName` construction (before the existing line ~376 `ImageFileName(photoFile.originalFilename ?: "")`):

```kotlin
        // Validate file content
        validateContentType(photoFile.contentType)
        validateImageMagicBytes(photoFile.bytes)
```

- [ ] **Step 3: Verify compilation**

Run: `mvn compile`
Expected: BUILD SUCCESS

- [ ] **Step 4: Commit**

```bash
git add src/main/kotlin/no/fg/hilflingbackend/value_object/ImageFileName.kt src/main/kotlin/no/fg/hilflingbackend/service/PhotoService.kt
git commit -m "fix: integrate file upload validation into PhotoService and ImageFileName"
```

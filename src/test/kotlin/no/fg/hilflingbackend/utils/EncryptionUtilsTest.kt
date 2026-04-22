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
    val plaintext = "photo-æøå-test"
    val encrypted = EncryptionUtils.encrypt(plaintext, testKey)
    val decrypted = EncryptionUtils.decrypt(encrypted, testKey)
    assertEquals(plaintext, decrypted)
  }
}

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

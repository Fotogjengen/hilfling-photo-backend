package no.fg.hilflingbackend.utils

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


// Functions for encrypting and decrypting data
object EncryptionUtils {
  private const val ALGORITHM = "AES/CBC/PKCS5Padding"
  private const val KEY_SIZE = 256
  private const val IV_SIZE = 16

  // Generate a new SecretKey (only once, store it securely)
  fun generateSecretKey(): SecretKey {
    val keyBytes = ByteArray(KEY_SIZE / 8) // 256-bit key
    java.security.SecureRandom().nextBytes(keyBytes)
    return SecretKeySpec(keyBytes, "AES")
  }

  // Encrypt a plain text string using AES
  fun encrypt(
    data: String,
    secretKey: SecretKey
  ): String {
    // Generate a random IV (Initialization Vector)
    val iv = ByteArray(IV_SIZE)
    java.security.SecureRandom().nextBytes(iv)
    val ivSpec = IvParameterSpec(iv)

    // Initialize Cipher in ENCRYPT_MODE
    val cipher = Cipher.getInstance(ALGORITHM)
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)

    // Encrypt data and concatenate IV with encrypted bytes
    val encryptedBytes = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
    val encryptedWithIv = iv + encryptedBytes

    // Encode to Base64 for safe string storage/transmission
    return Base64.getEncoder().encodeToString(encryptedWithIv)
  }

  // Decrypt a Base64 encoded string using AES
  fun decrypt(
    encryptedData: String,
    secretKey: SecretKey
  ): String {
    // Decode Base64 encoded data
    val decodedBytes = Base64.getDecoder().decode(encryptedData)

    // Extract the IV and encrypted message
    val iv = decodedBytes.copyOfRange(0, IV_SIZE)
    val encryptedBytes = decodedBytes.copyOfRange(IV_SIZE, decodedBytes.size)
    val ivSpec = IvParameterSpec(iv)

    // Initialize Cipher in DECRYPT_MODE
    val cipher = Cipher.getInstance(ALGORITHM)
    cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)

    // Decrypt the message
    val decryptedBytes = cipher.doFinal(encryptedBytes)
    return String(decryptedBytes, Charsets.UTF_8)
  }
}


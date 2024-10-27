package no.fg.hilflingbackend.configurations

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.security.SecureRandom
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
// Function for fetching encryption key from enviorments or creating a new encryption key
// TODO: Change so the secret encryption key only gets fetched and not created before launch
@Configuration
class SecurityConfig {
  @Bean
  fun secretKey(): SecretKey {
    // You can either load from environment or generate a random key
    val key = System.getenv("ENCRYPTION_KEY")
    return if (key != null) {
      // Make sure key length is valid for AES (16, 24, or 32 bytes)
      require(key.length == 16 || key.length == 24 || key.length == 32) {
        "Invalid key length: must be 16, 24, or 32 bytes long."
      }
      SecretKeySpec(key.toByteArray(), "AES")
    } else {
      // Generate a random key if environment variable is not set
      val keyBytes = ByteArray(16) // 128-bit AES key
      SecureRandom().nextBytes(keyBytes)
      SecretKeySpec(keyBytes, "AES")
    }
  }
}

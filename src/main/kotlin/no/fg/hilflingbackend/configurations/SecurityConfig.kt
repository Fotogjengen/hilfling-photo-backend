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

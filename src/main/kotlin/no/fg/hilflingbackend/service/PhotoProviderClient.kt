package no.fg.hilflingbackend.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.client.JdkClientHttpRequestFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import java.security.MessageDigest
import java.time.Duration

data class ImageUrls(
  val prod: String?,
  val web: String?,
  val thumb: String,
)

/** Where a photo's files should live */
data class PhotoLocation(
  val photoId: String,
  val current: ImageUrls,
  val securityLevel: String,
  val album: String,
  val pageNumber: Int,
  val imageNumber: Int,
)

data class RelocatedPhoto(
  val photoId: String,
  val imageProd: String?,
  val imageWeb: String?,
  val imageThumb: String,
)

data class PhotoFiles(
  val photoId: String,
  val current: ImageUrls,
)

private data class RelocateResponse(
  val photos: List<RelocatedPhoto>,
)

/**
 * Calls the photo-provider's internal endpoints. Each request is authorised by a service token bound to the
 * SHA-256 of the exact body bytes sent, so a captured token cannot be reused for a different plan.
 */
@Service
class PhotoProviderClient(
  @Value("\${photo-provider.url}") providerUrl: String,
  private val jwtService: JwtService,
  private val objectMapper: ObjectMapper,
) {
  private val restClient =
    RestClient
      .builder()
      .baseUrl(providerUrl.trimEnd('/'))
      .requestFactory(JdkClientHttpRequestFactory().apply { setReadTimeout(Duration.ofSeconds(120)) })
      .build()

  /**
   * Moves a number of files to a new location. Returns their new filepaths
   */
  fun relocate(photos: List<PhotoLocation>): List<RelocatedPhoto> {
    if (photos.isEmpty()) return emptyList()
    val response = post("relocate", mapOf("photos" to photos)) ?: error("Empty relocate response from photo-provider")
    return objectMapper.readValue<RelocateResponse>(response).photos
  }

  /** Deletes a number of photos from the provider */
  fun delete(photos: List<PhotoFiles>) {
    if (photos.isEmpty()) return
    post("delete", mapOf("photos" to photos))
  }

  private fun post(
    action: String,
    payload: Any,
  ): ByteArray? {
    val body = objectMapper.writeValueAsBytes(payload)
    val token = jwtService.generateServiceToken(scope = action, planHash = sha256Hex(body))
    return restClient
      .post()
      .uri("/api/provider-internal/photos/$action")
      .header("Authorization", "Bearer $token")
      .contentType(MediaType.APPLICATION_JSON)
      .body(body)
      .retrieve()
      .body(ByteArray::class.java)
  }

  private fun sha256Hex(bytes: ByteArray): String = MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }
}

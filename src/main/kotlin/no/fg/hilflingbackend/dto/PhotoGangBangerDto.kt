package no.fg.hilflingbackend.dto

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import no.fg.hilflingbackend.model.PhotoGangBanger
import no.fg.hilflingbackend.valueobject.SemesterStart
import java.util.UUID

data class PhotoGangBangerPatchRequestDto(
  val photoGangBangerId: PhotoGangBangerId,
  val semesterStart: SemesterStart?,
  val isActive: Boolean?,
  val isPang: Boolean?,
  val firstName: String?,
  val lastName: String?,
  val username: String?,
  val email: String?,
  val profilePicture: String?,
  val phoneNumber: String?,
)

data class PhotoGangBangerDto(
  val photoGangBangerId: PhotoGangBangerId = PhotoGangBangerId(),
  val semesterStart: SemesterStart,
  val isActive: Boolean,
  val isPang: Boolean,
  val firstName: String,
  val lastName: String,
  val username: String,
  val email: String,
  val profilePicture: String,
  val phoneNumber: String,
  val positions: List<MemberPositionDto> = emptyList(),
)

fun PhotoGangBangerDto.toEntity(): PhotoGangBanger =
  PhotoGangBanger {
    id = photoGangBangerId.id
    semesterStart = this@toEntity.semesterStart.value
    isPang = this@toEntity.isPang
    isActive = this@toEntity.isActive
    firstName = this@toEntity.firstName
    lastName = this@toEntity.lastName
    username = this@toEntity.username
    email = this@toEntity.email
    profilePicture = this@toEntity.profilePicture
    phoneNumber = this@toEntity.phoneNumber
  }

@JsonDeserialize(using = PhotoGangBangerIdDeserializer::class)
data class PhotoGangBangerId(
  override val id: UUID = UUID.randomUUID(),
) : UuidId {
  override fun toString(): String = id.toString()
}

class PhotoGangBangerIdDeserializer : JsonDeserializer<PhotoGangBangerId>() {
  override fun deserialize(
    parser: JsonParser,
    context: DeserializationContext,
  ): PhotoGangBangerId =
    when (parser.currentToken()) {
      JsonToken.START_OBJECT -> {
        val node = parser.codec.readTree<JsonNode>(parser)
        val idNode = node.get("id")
        val id = if (idNode == null || idNode.isNull) null else idNode.asText()
        id.toPhotoGangBangerId()
      }

      JsonToken.VALUE_STRING -> {
        parser.valueAsString.toPhotoGangBangerId()
      }

      JsonToken.VALUE_NULL -> {
        PhotoGangBangerId()
      }

      else -> {
        throw JsonMappingException.from(parser, "Expected PhotoGangBangerId object, string, or null")
      }
    }

  private fun String?.toPhotoGangBangerId(): PhotoGangBangerId =
    if (isNullOrBlank()) {
      PhotoGangBangerId()
    } else {
      PhotoGangBangerId(UUID.fromString(this))
    }
}

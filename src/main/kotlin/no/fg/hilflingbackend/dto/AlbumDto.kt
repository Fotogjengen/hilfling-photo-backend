package no.fg.hilflingbackend.dto


import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

import no.fg.hilflingbackend.model.Album
import java.util.UUID

data class AlbumPatchRequestDto(
  val albumId: AlbumId,
  val name: String,
  val description: String?,
  val analog: Boolean?,
  val isDefault: Boolean = false,
)

data class AlbumDto(
  val albumId: AlbumId = AlbumId(),
  val name: String,
  val description: String? = null,
  val analog: Boolean = false,
  val isDefault: Boolean = false,
)

// data class AlbumId(
//   override val id: UUID = UUID.randomUUID(),
// ) : UuidId {
//   override fun toString(): String = id.toString()
// }

fun AlbumDto.toEntity(): Album {
  val dto = this
  return Album {
    id = dto.albumId.id
    name = dto.name
    description = dto.description
    analog = dto.analog
    isDefault = dto.isDefault
  }
}
@JsonDeserialize(using = AlbumIdDeserializer::class)
data class AlbumId(
  override val id: UUID = UUID.randomUUID(),
) : UuidId {
  override fun toString(): String = id.toString()
}

class AlbumIdDeserializer : JsonDeserializer<AlbumId>() {
  override fun deserialize(
    parser: JsonParser,
    context: DeserializationContext,
  ): AlbumId =
    when (parser.currentToken()) {
      JsonToken.START_OBJECT -> {
        val node = parser.codec.readTree<JsonNode>(parser)
        val idNode = node.get("id")
        val id = if (idNode == null || idNode.isNull) null else idNode.asText()
        id.toAlbumId()
      }
      JsonToken.VALUE_STRING -> parser.valueAsString.toAlbumId()
      JsonToken.VALUE_NULL -> AlbumId()
      else -> throw JsonMappingException.from(parser, "Expected AlbumId object, string, or null")
    }

  private fun String?.toAlbumId(): AlbumId =
    if (isNullOrBlank()) AlbumId() else AlbumId(UUID.fromString(this))
}

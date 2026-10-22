package no.fg.hilflingbackend.dto

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import no.fg.hilflingbackend.model.Place
import java.util.UUID

data class PlacePatchRequestDto(
  val placeId: PlaceId,
  val name: String?,
)

data class PlaceDto(
  val placeId: PlaceId = PlaceId(),
  val name: String,
)

fun PlaceDto.toEntity(): Place {
  val dto = this
  return Place {
    id = dto.placeId.id
    name = dto.name
  }
}

@JsonDeserialize(using = PlaceIdDeserializer::class)
data class PlaceId(
  override val id: UUID = UUID.randomUUID(),
) : UuidId {
  override fun toString(): String = id.toString()
}

class PlaceIdDeserializer : JsonDeserializer<PlaceId>() {
  override fun deserialize(
    parser: JsonParser,
    context: DeserializationContext,
  ): PlaceId =
    when (parser.currentToken()) {
      JsonToken.START_OBJECT -> {
        val node = parser.codec.readTree<JsonNode>(parser)
        val idNode = node.get("id")
        val id = if (idNode == null || idNode.isNull) null else idNode.asText()
        id.toPlaceId()
      }
      JsonToken.VALUE_STRING -> parser.valueAsString.toPlaceId()
      JsonToken.VALUE_NULL -> PlaceId()
      else -> throw JsonMappingException.from(parser, "Expected PlaceId object, string, or null")
    }

  private fun String?.toPlaceId(): PlaceId =
    if (isNullOrBlank()) PlaceId() else PlaceId(UUID.fromString(this))
}
// data class PlaceId(
//   override val id: UUID = UUID.randomUUID(),
// ) : UuidId {
//   override fun toString(): String = id.toString()
// }
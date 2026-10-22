package no.fg.hilflingbackend.dto

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

import no.fg.hilflingbackend.model.Category
import java.util.UUID

data class CategoryPatchRequestDto(
  val categoryId: CategoryId,
  val name: String?,
)

data class CategoryDto(
  val categoryId: CategoryId = CategoryId(),
  val name: String,
)

fun CategoryDto.toEntity(): Category {
  val dto = this
  return Category {
    id = dto.categoryId.id
    name = dto.name
  }
}

@JsonDeserialize(using = CategoryIdDeserializer::class)
data class CategoryId(
  override val id: UUID = UUID.randomUUID(),
) : UuidId {
  override fun toString(): String = id.toString()
}

class CategoryIdDeserializer : JsonDeserializer<CategoryId>() {
  override fun deserialize(
    parser: JsonParser,
    context: DeserializationContext,
  ): CategoryId =
    when (parser.currentToken()) {
      JsonToken.START_OBJECT -> {
        val node = parser.codec.readTree<JsonNode>(parser)
        val idNode = node.get("id")
        val id = if (idNode == null || idNode.isNull) null else idNode.asText()
        id.toCategoryId()
      }

      JsonToken.VALUE_STRING -> {
        parser.valueAsString.toCategoryId()
      }

      JsonToken.VALUE_NULL -> {
        CategoryId()
      }

      else -> {
        throw JsonMappingException.from(parser, "Expected CategoryId object, string, or null")
      }
    }

  private fun String?.toCategoryId(): CategoryId = if (isNullOrBlank()) CategoryId() else CategoryId(UUID.fromString(this))
}
// data class CategoryId(
//   override val id: UUID = UUID.randomUUID(),
// ) : UuidId {
//   override fun toString(): String = id.toString()
// }

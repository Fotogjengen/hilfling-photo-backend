package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.SecurityLevel
import no.fg.hilflingbackend.value_object.SecurityLevelType
import java.util.UUID

data class SecurityLevelDto(
  val securityLevelId: SecurityLevelId,
  val securityLevelType: SecurityLevelType
) {
  fun toEntity(): SecurityLevel {
    val dto = this
    return SecurityLevel {
      id = dto.securityLevelId.id
      type = dto.securityLevelType.name
    }
  }
}
fun SecurityLevel.toDto() = SecurityLevelDto(
  securityLevelId = SecurityLevelId(this.id),
  securityLevelType = SecurityLevelType.valueOf(this.type)
)

data class SecurityLevelId(
  override val id: UUID = UUID.randomUUID()
) : UuidId {
  override fun toString(): String = id.toString()
}

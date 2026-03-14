package no.fg.hilflingbackend.value_object

import java.util.regex.Pattern
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

data class Email @JsonCreator constructor(
  @JsonValue val value: String,
) {
  companion object {
    operator fun invoke(value: String): Email {
      if (isEmailValid(value)) {
        return Email(value)
      }
      throw IllegalArgumentException("Not an email")
    }

    fun isEmailValid(email: String): Boolean =
      Pattern.compile(
        "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-zA-Z]{2,}$"
      ).matcher(email).matches()
  }
}

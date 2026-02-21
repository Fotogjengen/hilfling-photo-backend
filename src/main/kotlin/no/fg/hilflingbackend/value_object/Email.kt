package no.fg.hilflingbackend.value_object

import java.util.regex.Pattern

data class Email private constructor(
  val value: String,
) {
  companion object {
    operator fun invoke(value: String): Email {
      if (value != null && isEmailValid(value)) {
        return Email(value = value)
      }
      println("Invalid email passed to Email(): '$value'")

      throw IllegalArgumentException("Not an email")
    }

    fun isEmailValid(email: String): Boolean =
      Pattern
        .compile(
          "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-zA-Z]{2,}$",
        ).matcher(email)
        .matches()
  }
}

package no.fg.hilflingbackend.value_object

import java.util.regex.Pattern

data class PhoneNumber private constructor(val value: String) {
  companion object {
    operator fun invoke(value: String): PhoneNumber {
      if (value != null && isValidPhoneNumber(value)) {
        return PhoneNumber(value = value)
      }
      throw IllegalArgumentException("Not a phoneNumber!")
    }

    fun isValidPhoneNumber(email: String): Boolean {
      return Pattern.compile(
        "^[0-9]{8}$"
      ).matcher(email).matches()
    }
  }
}

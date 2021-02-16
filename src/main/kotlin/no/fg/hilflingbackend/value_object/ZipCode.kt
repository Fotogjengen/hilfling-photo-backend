package no.fg.hilflingbackend.value_object

import java.util.regex.Pattern

data class ZipCode private constructor(val value: String) {
  companion object {
    operator fun invoke(value: String): ZipCode {
      if (value != null && isZipCodeValid(value)) {
        return ZipCode(value = value)
      }
      throw IllegalArgumentException("Not a norwegian zip code")
    }

    fun isZipCodeValid(zipCode: String): Boolean {
      return Pattern.compile("^([0-9]{4})\$")
        .matcher(zipCode).matches()
    }
  }
}

package no.fg.hilflingbackend.value_object

import java.lang.IllegalArgumentException
import java.util.regex.Pattern

data class Email private constructor(val value: String) {
    companion object {
        fun create(value: String): Email {
            if(value != null && isEmailValid(value)){
                return Email(value = value)
            }
            throw IllegalArgumentException("Not an email")
        }
        fun isEmailValid(email: String): Boolean {
          return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
          ).matcher(email).matches()
        }

    }
}
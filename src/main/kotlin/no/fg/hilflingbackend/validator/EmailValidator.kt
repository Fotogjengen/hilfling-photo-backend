package no.fg.hilflingbackend.validator

import no.fg.hilflingbackend.annotation.Email
import java.util.regex.Pattern
import java.util.stream.Collectors
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/*
* Validator for email, invoked by @ValidEmail annotation
* */
class EmailValidator(
) : ConstraintValidator<Email, String> {

  // Regex pattern to validate email, this pattern is according to RFC 5322
  private val EMAIL_PATTERN_RFC5322 = "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"

  override fun isValid(email: String, context: ConstraintValidatorContext): Boolean {
    /*
    * @param email, Email to validate
    * @param context, Context passed back to frontend template with constraint messages
    * @return Boolean, is the email valid or not
    * */

    // Skip other validation if email is whitelisted
    val pattern = Pattern.compile(EMAIL_PATTERN_RFC5322)
    val matcher = pattern.matcher(email)
    val messages = ArrayList<String>() // Error messages passed to context (frontend)
    if (!matcher.matches()) {
      // Email does not match regex pattern
      messages.add("Email address must be a valid email")
    }
    if (messages.size < 1) {
      // No error messages, so we return true
      return true
    }

    // Build context with messages, separated with ";" to make it easy to map to a list in the frontend template
    val messageTemplate = messages.stream()
      .collect(Collectors.joining(";"))
    context.buildConstraintViolationWithTemplate(messageTemplate)
      .addConstraintViolation()
      .disableDefaultConstraintViolation()
    return false // return false, since we have at least 1 error message
  }
}

package no.fg.hilflingbackend.annotation

import no.fg.hilflingbackend.validator.EmailValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented // annotation is part of a public API
@Retention(AnnotationRetention.RUNTIME) // Invoke on runtime
@Target(AnnotationTarget.FIELD) // Annotation can be used on fields
@Constraint(validatedBy = [EmailValidator::class]) // Validated by CustomEmailValidator
annotation class Email(
  val message: String = "Invalid email",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = []
)

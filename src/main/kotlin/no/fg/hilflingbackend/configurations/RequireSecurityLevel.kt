package no.fg.hilflingbackend.configurations

import no.fg.hilflingbackend.valueobject.SecurityLevelType

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequireSecurityLevel(
  val value: SecurityLevelType,
  val allowExternalUsers: Boolean = false,
)

package no.fg.hilflingbackend.configurations

import no.fg.hilflingbackend.valueobject.Permission

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequirePermission(
  vararg val value: Permission,
)

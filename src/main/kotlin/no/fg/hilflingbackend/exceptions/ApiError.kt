package no.fg.hilflingbackend.exceptions

import org.springframework.http.HttpStatus

data class ApiError(
  val message: String,
  val debugMessage: String,
  val status: HttpStatus,
)

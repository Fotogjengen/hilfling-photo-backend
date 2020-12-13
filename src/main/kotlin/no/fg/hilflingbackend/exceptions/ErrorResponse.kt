package no.fg.hilflingbackend.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import java.util.ArrayList
import java.util.Date

data class ErrorResponse(
  val status: HttpStatus,
  val error: String,
  val message: String,
  val timestamp: Date,
  val bindingErrors: List<String>
) {

  constructor(status: HttpStatus, message: String, bindingErrors: List<String>) : this(status, status.reasonPhrase, message, Date(), bindingErrors)
  constructor(status: HttpStatus, error: String, message: String) : this(status, error, message, Date(), ArrayList<String>())
  constructor(status: HttpStatus, message: String) : this(status, status.reasonPhrase, message, Date(), ArrayList<String>())
}

class ErrorResponseEntity : ResponseEntity<ErrorResponse> {

  constructor(body: ErrorResponse) : super(body, body.status)
  constructor(body: ErrorResponse, headers: MultiValueMap<String, String>) : super(body, headers, body.status)

  companion object {

    fun badReqeust(message: String): ResponseEntity<Any> = ResponseEntity(ErrorResponse(HttpStatus.BAD_REQUEST, message), HttpStatus.BAD_REQUEST)
    fun badReqeust(message: String, bindingErrors: List<String>) = ResponseEntity(ErrorResponse(HttpStatus.BAD_REQUEST, message, bindingErrors), HttpStatus.BAD_REQUEST)
    fun notFound(message: String) = ResponseEntity<Any>(ErrorResponse(HttpStatus.NOT_FOUND, message), HttpStatus.NOT_FOUND)
    fun serverError(message: String): ResponseEntity<Any> = ResponseEntity(ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, message), HttpStatus.INTERNAL_SERVER_ERROR)
  }
}

package no.fg.hilflingbackend.exceptions

import no.fg.hilflingbackend.exceptions.ErrorResponseEntity.Companion.badReqeust
import no.fg.hilflingbackend.exceptions.ErrorResponseEntity.Companion.serverError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

open class GlobalExceptionHandler: ResponseEntityExceptionHandler() {

  @ExceptionHandler(value = [(MissingServletRequestParameterException::class)])
  fun missingParameterExceptionHandler(exception: MissingServletRequestParameterException): ErrorResponseEntity {
    println("test")
    return serverError(exception.localizedMessage)

  }

}

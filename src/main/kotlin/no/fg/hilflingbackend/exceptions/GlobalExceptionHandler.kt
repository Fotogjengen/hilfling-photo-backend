package no.fg.hilflingbackend.exceptions

import no.fg.hilflingbackend.exceptions.ErrorResponseEntity.Companion.badReqeust
import no.fg.hilflingbackend.exceptions.ErrorResponseEntity.Companion.notFound
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.persistence.EntityNotFoundException

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
open class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

  @ExceptionHandler(value = [(EntityNotFoundException::class)])
  fun globalExceptionHandler(
    ex: EntityNotFoundException,
  ): ResponseEntity<Any> {
    println("Global exception handler")
    return notFound(ex?.message ?: "Something went wrong")
  }

  @Override
  override fun handleMissingServletRequestParameter(
    ex: MissingServletRequestParameterException,
    headers: HttpHeaders,
    status: HttpStatus,
    request: WebRequest
  ): ResponseEntity<Any> {
    return badReqeust(ex.localizedMessage)
  }
  // TODO: Add service not available error

  // TODO : Add unauthorized error
}

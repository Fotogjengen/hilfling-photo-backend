package no.fg.hilflingbackend.exceptions

import no.fg.hilflingbackend.exceptions.ErrorResponseEntity.Companion.badReqeust
import no.fg.hilflingbackend.exceptions.ErrorResponseEntity.Companion.notFound
import org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException
import org.slf4j.LoggerFactory
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
import jakarta.persistence.EntityNotFoundException
import java.lang.IllegalArgumentException

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
open class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

  private val log = LoggerFactory.getLogger(this::class.java)

  @ExceptionHandler(value = [(EntityNotFoundException::class)])
  fun globalExceptionHandler(
    ex: EntityNotFoundException
  ): ResponseEntity<Any> {
    log.error("Entity not found", ex)
    return notFound("Not found")
  }

  @ExceptionHandler(value = [(IllegalArgumentException::class)])
  fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<Any> {
    log.warn("Illegal argument", ex)
    return badReqeust("Invalid request")
  }

  // Handles missing request parameters
  @Override
  fun handleMissingServletRequestParameter(
    ex: MissingServletRequestParameterException,
    headers: HttpHeaders,
    status: HttpStatus,
    request: WebRequest
  ): ResponseEntity<Any> {
    log.error("Missing request parameter", ex)
    return badReqeust("Missing required parameter")
  }

  // Handles missing multipart form HEADER when uploading photos in PhotoController
  @ExceptionHandler(value = [(InvalidContentTypeException::class)])
  fun handleInvalidContentTypeException(ex: InvalidContentTypeException): ResponseEntity<Any> {
    log.warn("Invalid content type", ex)
    return badReqeust("Invalid content type")
  }
  // TODO: Add service not available error

  // TODO : Add unauthorized error
}

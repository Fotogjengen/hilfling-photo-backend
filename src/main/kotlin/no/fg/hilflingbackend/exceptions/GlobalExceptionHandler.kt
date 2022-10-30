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
import java.lang.IllegalArgumentException
import javax.persistence.EntityNotFoundException

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
open class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

  val log = LoggerFactory.getLogger(this::class.java)

  @ExceptionHandler(value = [(EntityNotFoundException::class)])
  fun globalExceptionHandler(
    ex: EntityNotFoundException
  ): ResponseEntity<Any> {
    log.error(ex.localizedMessage)
    return notFound(ex?.message ?: "Something went wrong")
  }

  @ExceptionHandler(value = [(IllegalArgumentException::class)])
  fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<Any> {
    log.warn(ex.toString())
    return badReqeust(ex.localizedMessage)
  }

  // Handles missing request parameters
  @Override
  override fun handleMissingServletRequestParameter(
    ex: MissingServletRequestParameterException,
    headers: HttpHeaders,
    status: HttpStatus,
    request: WebRequest
  ): ResponseEntity<Any> {
    log.error(ex.localizedMessage)
    return badReqeust(ex.localizedMessage)
  }

  // Handles missing multipart form HEADER when uploading photos in PhotoController
  @ExceptionHandler(value = [(InvalidContentTypeException::class)])
  fun handleInvalidContentTypeException(ex: InvalidContentTypeException): ResponseEntity<Any> {
    logger.warn(ex.localizedMessage)
    return badReqeust(ex.localizedMessage)
  }
  // TODO: Add service not available error

  // TODO : Add unauthorized error
}

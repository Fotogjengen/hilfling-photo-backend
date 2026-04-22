package hilfling.backend.hilfling.exceptions

import jakarta.persistence.EntityNotFoundException
import jakarta.validation.ConstraintViolationException
import no.fg.hilflingbackend.exceptions.ApiError
import no.fg.hilflingbackend.exceptions.EntityCreationException
import no.fg.hilflingbackend.exceptions.EntityExistsException
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice // This annotation makes the class handle all exceptions
class RestExceptionHandler : ResponseEntityExceptionHandler() {
  private val log = LoggerFactory.getLogger(this::class.java)

  /*
      The function of this class is to handle all exceptions that the service layer throws.
      it extends ResponseEntityExceptionHandler which has a lot of methods out of the box.
      But we can also add or improve exceptionsHandlers by adding them like we do below.
   */

  fun handleHttpMessageNotReadable(
    ex: HttpMessageNotReadableException,
    headers: HttpHeaders,
    status: HttpStatus,
    request: WebRequest
  ): ResponseEntity<Any> {
    log.error("Malformed JSON request", ex)
    val apiError = ApiError("Malformed JSON request", "", HttpStatus.BAD_REQUEST)
    return ResponseEntity(apiError, apiError.status)
  }

  @ExceptionHandler(value = [(EntityNotFoundException::class)]) // this defines which exceptions we want to improve
  fun handleEntityNotFound(ex: EntityNotFoundException): ResponseEntity<Any> {
    log.error("Entity not found", ex)
    val error = ApiError("Not found", "", HttpStatus.NOT_FOUND)
    return ResponseEntity(error, error.status)
  }

  @ExceptionHandler(EmptyResultDataAccessException::class)
  fun handleDeleteObjectThatDoesNotExist(ex: EmptyResultDataAccessException): ResponseEntity<Any> {
    log.error("Attempted to access non-existent object", ex)
    val error = ApiError("No object with that id exists", "", HttpStatus.NOT_FOUND)
    return ResponseEntity(error, error.status)
  }
  // other exception handlers below

  @ExceptionHandler(DataIntegrityViolationException::class)
  fun handleDataIntegrityViolation(ex: DataIntegrityViolationException): ResponseEntity<Any> {
    log.error("Database integrity violation", ex)
    val error = ApiError("A database error occurred", "", HttpStatus.INTERNAL_SERVER_ERROR)
    return ResponseEntity(error, error.status)
  }

  @ExceptionHandler(ConstraintViolationException::class)
  fun handleConstraintViolation(ex: ConstraintViolationException): ResponseEntity<Any> {
    log.error("Constraint violation", ex)
    val error = ApiError("Violates constraint", "", HttpStatus.BAD_REQUEST)
    return ResponseEntity(error, error.status)
  }

  @ExceptionHandler(EntityExistsException::class)
  fun handleEntityExist(ex: EntityExistsException): ResponseEntity<Any> {
    log.error("Entity already exists", ex)
    val error = ApiError("Entity already exists", "", HttpStatus.BAD_REQUEST)
    return ResponseEntity(error, error.status)
  }

  @ExceptionHandler(EntityCreationException::class)
  fun handleCreationFailed(ex: EntityCreationException): ResponseEntity<Any> {
    log.error("Entity creation failed", ex)
    val error = ApiError("Entity could not be created", "", HttpStatus.BAD_REQUEST)
    return ResponseEntity(error, error.status)
  }
}

package hilfling.backend.hilfling.exceptions

import jakarta.persistence.EntityNotFoundException
import jakarta.validation.ConstraintViolationException
import no.fg.hilflingbackend.exceptions.ApiError
import no.fg.hilflingbackend.exceptions.EntityCreationException
import no.fg.hilflingbackend.exceptions.EntityExistsException
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
    val error = "Malformed JSON request"
    val apiError: ApiError = ApiError(error, "", HttpStatus.BAD_REQUEST, ex)
    return ResponseEntity(apiError, apiError.status)
  }

  @ExceptionHandler(value = [(EntityNotFoundException::class)]) // this defines which exceptions we want to improve
  fun handleEntityNotFound(ex: EntityNotFoundException): ResponseEntity<Any> {
    val error = ApiError("Not found", "", HttpStatus.NOT_FOUND, ex)
    return ResponseEntity(error, error.status)
  }

  @ExceptionHandler(EmptyResultDataAccessException::class)
  fun handleDeleteObjectThatDoesNotExist(ex: EmptyResultDataAccessException): ResponseEntity<Any> {
    val error = ApiError("No object with tat id exist", "", HttpStatus.NOT_FOUND, ex)
    return ResponseEntity(error, error.status)
  }
  // other exception handlers below

  @ExceptionHandler(DataIntegrityViolationException::class)
  fun handleDataIntegrityViolation(ex: DataIntegrityViolationException): ResponseEntity<Any> {
    val error = ApiError(String.format("Database error %s", ex.rootCause), "", HttpStatus.INTERNAL_SERVER_ERROR, ex)
    return ResponseEntity(error, error.status)
  }

  @ExceptionHandler(ConstraintViolationException::class)
  fun handleConstraintViolation(ex: ConstraintViolationException): ResponseEntity<Any> {
    val error = ApiError("Violates constraint", "", HttpStatus.BAD_REQUEST, ex)
    return ResponseEntity(error, error.status)
  }

  @ExceptionHandler(EntityExistsException::class)
  fun handleEntityExist(ex: EntityExistsException): ResponseEntity<Any> {
    val error = ApiError(String.format("Entity exists: %s", ex.message), "", HttpStatus.BAD_REQUEST, ex)
    return ResponseEntity(error, error.status)
  }

  @ExceptionHandler(EntityCreationException::class)
  fun handleCreationFailed(ex: EntityCreationException): ResponseEntity<Any> {
    val error = ApiError(String.format("Entity could not be created: %s", ex.message), "", HttpStatus.BAD_REQUEST, ex)
    return ResponseEntity(error, error.status)
  }
}

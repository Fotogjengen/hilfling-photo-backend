package hilfling.backend.hilfing.exceptions

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.persistence.EntityNotFoundException

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {

    protected override fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val error = "Malformed JSON request"
        val apiError: ApiError = ApiError(error, "", HttpStatus.BAD_REQUEST, ex)
        return ResponseEntity(apiError, apiError.status)
    }

    @ExceptionHandler(value = [(EntityNotFoundException::class)])
    protected fun handleEntityNotFound(ex: EntityNotFoundException) : ResponseEntity<Any> {
        val error = ApiError("Not found", "", HttpStatus.NOT_FOUND, ex)
        return ResponseEntity(error, error.status)
    }

    @ExceptionHandler(EmptyResultDataAccessException::class)
    protected  fun handleDeleteObjectThatDoesNotExist(ex: EmptyResultDataAccessException) : ResponseEntity<Any> {
    val error = ApiError("No object with tat id exist", "", HttpStatus.NOT_FOUND, ex)
    return ResponseEntity(error, error.status)

    }

    private fun buildResponseEntity(apiError: ApiError): ResponseEntity<Any> {
        return ResponseEntity(apiError, apiError.status);
    }

    //other exception handlers below

}

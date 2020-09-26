package hilfling.backend.hilfling.exceptions

import org.springframework.http.HttpStatus

// Datacalss for ErrorMessages
data class ApiError(val message: String,
                    val debugMessage: String,
                    val status: HttpStatus,
                    val ex: Throwable) {
}
package hilfling.backend.hilfing.exceptions

import org.springframework.http.HttpStatus

data class ApiError(val message: String,
                    val debugMessage: String,
                    val status: HttpStatus,
                    val ex: Throwable) {
}
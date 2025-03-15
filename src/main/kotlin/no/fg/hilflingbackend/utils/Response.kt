package no.fg.hilflingbackend.utils

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ResponseOk<T> : ResponseEntity<T> {
  constructor(
    t: T,
    headers: HttpHeaders = HttpHeaders(),
    status: HttpStatus = HttpStatus.ACCEPTED,
  ) : super(t, headers, status)
}

class ResponseCreated<T> : ResponseEntity<T> {
  constructor(
    t: T,
    headers: HttpHeaders = HttpHeaders(),
    status: HttpStatus = HttpStatus.CREATED,
  ) : super(t, headers, status)
}

class ResponseNotCreated<T> : ResponseEntity<T> {
  constructor(
    t: T,
    headers: HttpHeaders = HttpHeaders(),
    status: HttpStatus = HttpStatus.BAD_REQUEST,
  ) : super(t, headers, status)
}

class ResponseUpdated<T> : ResponseEntity<T> {
  constructor(
    t: T,
    headers: HttpHeaders = HttpHeaders(),
    status: HttpStatus = HttpStatus.ACCEPTED,
  ) : super(t, headers, status)
}

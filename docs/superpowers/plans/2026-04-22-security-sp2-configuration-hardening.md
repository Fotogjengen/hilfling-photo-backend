# Sub-project 2: Configuration Hardening — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Harden application configuration: disable verbose errors, add security headers, configure CORS, protect Swagger, clean up default credentials, remove debug println statements, and sanitize exception messages.

**Architecture:** Mostly configuration and filter changes. A new `application-dev.yml` profile holds dev-only settings. A servlet filter adds security headers. CORS gets explicit configuration. Exception handlers are sanitized.

**Tech Stack:** Kotlin, Spring Boot 4, Spring MVC (WebMvcConfigurer, Filter)

---

## File Structure

| Action | Path | Responsibility |
|--------|------|----------------|
| Modify | `src/main/resources/application.yml` | Production-safe defaults |
| Create | `src/main/resources/application-dev.yml` | Dev-only verbose settings |
| Create | `src/main/kotlin/no/fg/hilflingbackend/configurations/SecurityHeadersFilter.kt` | HTTP security headers |
| Create | `src/main/kotlin/no/fg/hilflingbackend/configurations/CorsConfig.kt` | CORS configuration |
| Modify | `src/main/kotlin/no/fg/hilflingbackend/exceptions/RestExceptionHandler.kt` | Sanitize error messages |
| Modify | `src/main/kotlin/no/fg/hilflingbackend/exceptions/GlobalExceptionHandler.kt` | Sanitize error messages |
| Modify | `src/main/kotlin/no/fg/hilflingbackend/exceptions/ApiError.kt` | Remove Throwable from response |
| Modify | `src/main/kotlin/no/fg/hilflingbackend/service/PhotoService.kt` | Remove println calls |
| Modify | `src/main/kotlin/no/fg/hilflingbackend/repository/EventCardRepository.kt` | Remove println call |
| Modify | `docker-compose.yml` | PgAdmin credential cleanup |

---

### Task 1: Create Dev Profile and Harden Default Config

**Files:**
- Modify: `src/main/resources/application.yml`
- Create: `src/main/resources/application-dev.yml`

- [ ] **Step 1: Update application.yml — error handling**

In `src/main/resources/application.yml`, replace lines 8-12:

```yaml
  error:
    include-message: always
    include-stacktrace: always
    include-exception: true
    include-binding-errors: always
```

with:

```yaml
  error:
    include-message: never
    include-stacktrace: never
    include-exception: false
    include-binding-errors: never
```

- [ ] **Step 2: Update application.yml — logging levels**

In `src/main/resources/application.yml`, replace lines 50-60:

```yaml
logging:
  level:
    # root: debug
    # me.liuwj.ktorm: debug
    # org.springframework.web: DEBUG
    # org.springdoc: DEBUG
    root: info
    org.flywaydb: debug
    org.springframework.jdbc.datasource.init: debug
    org.springframework.web: debug
    org.springdoc: debug
```

with:

```yaml
logging:
  level:
    root: info
    org.flywaydb: info
    org.springframework.jdbc.datasource.init: info
    org.springframework.web: warn
    org.springdoc: warn
```

- [ ] **Step 3: Update application.yml — Swagger defaults**

In `src/main/resources/application.yml`, replace lines 62-68:

```yaml
springdoc:
  api-docs:
    path: "/v3/api-docs"
    enabled: true
  swagger-ui:
    path: "/"
    enabled: true
```

with:

```yaml
springdoc:
  api-docs:
    path: "/v3/api-docs"
    enabled: false
  swagger-ui:
    path: "/swagger-ui"
    enabled: false
```

- [ ] **Step 4: Update application.yml — remove default credentials**

In `src/main/resources/application.yml`, replace lines 14-21:

```yaml
spring:
  datasource:
    hikari:
      connection-timeout: 20000
      maximum-pool-size: 5
    username: ${DATABASE_USERNAME:hilfling}
    url: ${DATABASE_URL:jdbc:postgresql://postgres:5432/hilflingdb}
    password: ${DATABASE_PASSWORD:password}
    driver-class-name: ${DATABASE_DRIVER:org.postgresql.Driver}
```

with:

```yaml
spring:
  datasource:
    hikari:
      connection-timeout: 20000
      maximum-pool-size: 5
    username: ${DATABASE_USERNAME}
    url: ${DATABASE_URL}
    password: ${DATABASE_PASSWORD}
    driver-class-name: ${DATABASE_DRIVER:org.postgresql.Driver}
```

And replace the flyway section (lines 23-29):

```yaml
  flyway:
    user: ${DATABASE_USERNAME:hilfling}
    url: ${DATABASE_URL:jdbc:postgresql://postgres:5432/hilflingdb?createDatabaseIfNotExist=true}
    password: ${DATABASE_PASSWORD:password}
```

with:

```yaml
  flyway:
    user: ${DATABASE_USERNAME}
    url: ${DATABASE_URL}
    password: ${DATABASE_PASSWORD}
```

- [ ] **Step 5: Create application-dev.yml**

Create `src/main/resources/application-dev.yml`:

```yaml
# Dev profile — verbose logging and tooling enabled.
# Activate with: SPRING_PROFILES_ACTIVE=dev

spring:
  datasource:
    username: ${DATABASE_USERNAME:hilfling}
    url: ${DATABASE_URL:jdbc:postgresql://postgres:5432/hilflingdb}
    password: ${DATABASE_PASSWORD:password}
  flyway:
    user: ${DATABASE_USERNAME:hilfling}
    url: ${DATABASE_URL:jdbc:postgresql://postgres:5432/hilflingdb?createDatabaseIfNotExist=true}
    password: ${DATABASE_PASSWORD:password}

server:
  error:
    include-message: always
    include-stacktrace: always
    include-exception: true
    include-binding-errors: always

logging:
  level:
    root: info
    org.flywaydb: debug
    org.springframework.jdbc.datasource.init: debug
    org.springframework.web: debug
    org.springdoc: debug

springdoc:
  api-docs:
    enabled: true
  swagger-ui:
    enabled: true
```

- [ ] **Step 6: Verify compilation**

Run: `mvn compile`
Expected: BUILD SUCCESS

- [ ] **Step 7: Commit**

```bash
git add src/main/resources/application.yml src/main/resources/application-dev.yml
git commit -m "fix: harden default config — disable verbose errors, debug logging, and Swagger in production"
```

---

### Task 2: Add Security Headers Filter

**Files:**
- Create: `src/main/kotlin/no/fg/hilflingbackend/configurations/SecurityHeadersFilter.kt`

- [ ] **Step 1: Create the filter**

Create `src/main/kotlin/no/fg/hilflingbackend/configurations/SecurityHeadersFilter.kt`:

```kotlin
package no.fg.hilflingbackend.configurations

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(1)
class SecurityHeadersFilter : Filter {
  override fun doFilter(
    request: ServletRequest,
    response: ServletResponse,
    chain: FilterChain,
  ) {
    val httpResponse = response as HttpServletResponse
    httpResponse.setHeader("X-Content-Type-Options", "nosniff")
    httpResponse.setHeader("X-Frame-Options", "DENY")
    httpResponse.setHeader("X-XSS-Protection", "0")
    httpResponse.setHeader("Content-Security-Policy", "default-src 'self'")
    httpResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains")
    chain.doFilter(request, response)
  }
}
```

- [ ] **Step 2: Verify compilation**

Run: `mvn compile`
Expected: BUILD SUCCESS

- [ ] **Step 3: Commit**

```bash
git add src/main/kotlin/no/fg/hilflingbackend/configurations/SecurityHeadersFilter.kt
git commit -m "feat: add HTTP security headers filter"
```

---

### Task 3: Add CORS Configuration

**Files:**
- Create: `src/main/kotlin/no/fg/hilflingbackend/configurations/CorsConfig.kt`

- [ ] **Step 1: Create CORS configuration**

Create `src/main/kotlin/no/fg/hilflingbackend/configurations/CorsConfig.kt`:

```kotlin
package no.fg.hilflingbackend.configurations

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig : WebMvcConfigurer {
  override fun addCorsMappings(registry: CorsRegistry) {
    val allowedOrigins = System.getenv("ALLOWED_ORIGINS")
      ?.split(",")
      ?.map { it.trim() }
      ?.filter { it.isNotBlank() }
      ?.toTypedArray()
      ?: emptyArray()

    registry.addMapping("/**")
      .allowedOrigins(*allowedOrigins)
      .allowedMethods("GET", "POST", "PATCH", "DELETE", "OPTIONS")
      .allowedHeaders("*")
      .maxAge(3600)
  }
}
```

- [ ] **Step 2: Verify compilation**

Run: `mvn compile`
Expected: BUILD SUCCESS

- [ ] **Step 3: Commit**

```bash
git add src/main/kotlin/no/fg/hilflingbackend/configurations/CorsConfig.kt
git commit -m "feat: add CORS configuration with configurable allowed origins"
```

---

### Task 4: Sanitize Exception Handlers

**Files:**
- Modify: `src/main/kotlin/no/fg/hilflingbackend/exceptions/ApiError.kt`
- Modify: `src/main/kotlin/no/fg/hilflingbackend/exceptions/RestExceptionHandler.kt`
- Modify: `src/main/kotlin/no/fg/hilflingbackend/exceptions/GlobalExceptionHandler.kt`

- [ ] **Step 1: Remove Throwable from ApiError response body**

Replace the content of `src/main/kotlin/no/fg/hilflingbackend/exceptions/ApiError.kt`:

```kotlin
package no.fg.hilflingbackend.exceptions

import org.springframework.http.HttpStatus

data class ApiError(
  val message: String,
  val debugMessage: String,
  val status: HttpStatus,
)
```

- [ ] **Step 2: Update RestExceptionHandler to sanitize messages**

Replace the content of `src/main/kotlin/no/fg/hilflingbackend/exceptions/RestExceptionHandler.kt`:

```kotlin
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
@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {

  private val log = LoggerFactory.getLogger(this::class.java)

  fun handleHttpMessageNotReadable(
    ex: HttpMessageNotReadableException,
    headers: HttpHeaders,
    status: HttpStatus,
    request: WebRequest,
  ): ResponseEntity<Any> {
    val error = ApiError("Malformed JSON request", "", HttpStatus.BAD_REQUEST)
    return ResponseEntity(error, error.status)
  }

  @ExceptionHandler(value = [(EntityNotFoundException::class)])
  fun handleEntityNotFound(ex: EntityNotFoundException): ResponseEntity<Any> {
    val error = ApiError("Not found", "", HttpStatus.NOT_FOUND)
    return ResponseEntity(error, error.status)
  }

  @ExceptionHandler(EmptyResultDataAccessException::class)
  fun handleDeleteObjectThatDoesNotExist(ex: EmptyResultDataAccessException): ResponseEntity<Any> {
    val error = ApiError("No object with that id exists", "", HttpStatus.NOT_FOUND)
    return ResponseEntity(error, error.status)
  }

  @ExceptionHandler(DataIntegrityViolationException::class)
  fun handleDataIntegrityViolation(ex: DataIntegrityViolationException): ResponseEntity<Any> {
    log.error("Database integrity violation", ex)
    val error = ApiError("A database error occurred", "", HttpStatus.INTERNAL_SERVER_ERROR)
    return ResponseEntity(error, error.status)
  }

  @ExceptionHandler(ConstraintViolationException::class)
  fun handleConstraintViolation(ex: ConstraintViolationException): ResponseEntity<Any> {
    val error = ApiError("Violates constraint", "", HttpStatus.BAD_REQUEST)
    return ResponseEntity(error, error.status)
  }

  @ExceptionHandler(EntityExistsException::class)
  fun handleEntityExist(ex: EntityExistsException): ResponseEntity<Any> {
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
```

- [ ] **Step 3: Update GlobalExceptionHandler to not expose exception details**

Replace the content of `src/main/kotlin/no/fg/hilflingbackend/exceptions/GlobalExceptionHandler.kt`:

```kotlin
package no.fg.hilflingbackend.exceptions

import no.fg.hilflingbackend.exceptions.ErrorResponseEntity.Companion.badReqeust
import no.fg.hilflingbackend.exceptions.ErrorResponseEntity.Companion.notFound
import no.fg.hilflingbackend.exceptions.ErrorResponseEntity.Companion.serverError
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

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
open class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

  val log = LoggerFactory.getLogger(this::class.java)

  @ExceptionHandler(value = [(EntityNotFoundException::class)])
  fun globalExceptionHandler(
    ex: EntityNotFoundException,
  ): ResponseEntity<Any> {
    log.error("Entity not found", ex)
    return notFound("Not found")
  }

  @ExceptionHandler(value = [(IllegalArgumentException::class)])
  fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<Any> {
    log.warn("Invalid argument: {}", ex.message)
    return badReqeust("Invalid request")
  }

  @Override
  fun handleMissingServletRequestParameter(
    ex: MissingServletRequestParameterException,
    headers: HttpHeaders,
    status: HttpStatus,
    request: WebRequest,
  ): ResponseEntity<Any> {
    log.error("Missing request parameter: {}", ex.parameterName)
    return badReqeust("Missing required parameter")
  }

  @ExceptionHandler(value = [(InvalidContentTypeException::class)])
  fun handleInvalidContentTypeException(ex: InvalidContentTypeException): ResponseEntity<Any> {
    log.warn("Invalid content type: {}", ex.message)
    return badReqeust("Invalid content type")
  }
}
```

- [ ] **Step 4: Verify compilation**

Run: `mvn compile`
Expected: BUILD SUCCESS

- [ ] **Step 5: Commit**

```bash
git add src/main/kotlin/no/fg/hilflingbackend/exceptions/ApiError.kt src/main/kotlin/no/fg/hilflingbackend/exceptions/RestExceptionHandler.kt src/main/kotlin/no/fg/hilflingbackend/exceptions/GlobalExceptionHandler.kt
git commit -m "fix: sanitize exception messages — return generic errors, log details server-side"
```

---

### Task 5: Remove println Statements

**Files:**
- Modify: `src/main/kotlin/no/fg/hilflingbackend/service/PhotoService.kt`
- Modify: `src/main/kotlin/no/fg/hilflingbackend/repository/EventCardRepository.kt`

- [ ] **Step 1: Remove println from PhotoService.kt**

In `src/main/kotlin/no/fg/hilflingbackend/service/PhotoService.kt`:

- Delete line 84: `println("BaseBath from config: $basePath")`
- Delete line 93: `println(fullFilePath)`
- Delete line 362: `println(photoTagDtos)`

- [ ] **Step 2: Remove println from EventCardRepository.kt**

In `src/main/kotlin/no/fg/hilflingbackend/repository/EventCardRepository.kt`:

Delete lines 57-59:
```kotlin
        println(
          "Motive ID: $motiveId → Selected Small URL: $selectedPhotoUrl",
        ) // Debugging
```

- [ ] **Step 3: Verify compilation**

Run: `mvn compile`
Expected: BUILD SUCCESS

- [ ] **Step 4: Commit**

```bash
git add src/main/kotlin/no/fg/hilflingbackend/service/PhotoService.kt src/main/kotlin/no/fg/hilflingbackend/repository/EventCardRepository.kt
git commit -m "fix: remove debug println statements that leak internal paths"
```

---

### Task 6: Docker Compose — PgAdmin Credential Cleanup

**Files:**
- Modify: `docker-compose.yml`

- [ ] **Step 1: Update PgAdmin environment variables**

In `docker-compose.yml`, replace lines 89-91:

```yaml
    environment:
      PGADMIN_DEFAULT_EMAIL: ${PGADMIN_DEFAULT_EMAIL:-admin@admin.com}
      PGADMIN_DEFAULT_PASSWORD: ${PGADMIN_DEFAULT_PASSWORD:-password}
```

with:

```yaml
    # NOTE: PgAdmin is for development only. Do not include in production deployments.
    environment:
      PGADMIN_DEFAULT_EMAIL: ${PGADMIN_DEFAULT_EMAIL}
      PGADMIN_DEFAULT_PASSWORD: ${PGADMIN_DEFAULT_PASSWORD}
```

- [ ] **Step 2: Add dev profile to backend in docker-compose.yml**

In `docker-compose.yml`, add `SPRING_PROFILES_ACTIVE: dev` to the backend environment (after line 49):

```yaml
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/hilflingdb
      SPRING_DATASOURCE_USERNAME: ${DATABASE_USERNAME:-hilfling}
      SPRING_DATASOURCE_PASSWORD: ${DATABASE_PASSWORD:-password}
      SPRING_PROFILES_ACTIVE: dev
```

- [ ] **Step 3: Commit**

```bash
git add docker-compose.yml
git commit -m "fix: remove default PgAdmin credentials, activate dev profile for local docker"
```

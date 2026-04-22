# Security Remediation Design Spec

**Date:** 2026-04-22
**Scope:** All non-authentication security fixes identified in OWASP audit
**Approach:** Layered by severity — 4 sub-projects, each independently shippable

## Context

A full OWASP-focused security audit identified 23 findings across the hilfling-photo-backend codebase. Authentication/authorization (OWASP A01/A07) is explicitly out of scope for now. This spec covers everything else: injection, cryptographic failures, security misconfiguration, file upload hardening, API hardening, and infrastructure.

The application is pre-launch, so breaking changes are acceptable.

---

## Sub-project 1: Critical Code Fixes

### 1.1 SQL Injection Fix

**Problem:** Three repositories use string interpolation in `ilike` clauses, allowing injection:
- `SearchRepository.kt:23` — `it.title ilike "%$searchTerm%"`
- `EventCardRepository.kt:89` — `Motives.title ilike "%$searchTerm%"`
- `SearchSuggestionsRepository.kt:28` — `it.title ilike "%$searchTerm%"`

**Fix:** Sanitize the search term by escaping LIKE-special characters (`%`, `_`, `\`) before constructing the pattern. Create a shared utility function for LIKE escaping and apply it in all three repositories.

**Files changed:**
- `repository/SearchRepository.kt`
- `repository/EventCardRepository.kt`
- `repository/SearchSuggestionsRepository.kt`
- New: `utils/SqlUtils.kt` (escape function)

### 1.2 Encryption Upgrade

**Problem:**
- `EncryptionUtils.kt` uses AES/CBC/PKCS5Padding — vulnerable to padding oracle attacks, no integrity protection
- `SecurityConfig.kt` validates key string length instead of byte length
- If `ENCRYPTION_KEY` env var is missing, a random throwaway key is generated silently

**Fix:**
- Migrate from `AES/CBC/PKCS5Padding` to `AES/GCM/NoPadding` (authenticated encryption with integrity)
- Fix key validation: check `key.toByteArray().size` instead of `key.length`
- Fail fast: throw `IllegalStateException` at startup if `ENCRYPTION_KEY` is not set — no silent fallback

**Files changed:**
- `utils/EncryptionUtils.kt`
- `configurations/SecurityConfig.kt`

### 1.3 Seed Endpoint Gating

**Problem:** `GET /seed` resets the entire database — accessible to anyone, no protection.

**Fix:** Add `@Profile("dev", "test")` to `SeedController` so it is only registered when running in dev or test profiles.

**Files changed:**
- `controller/SeedController.kt`

### 1.4 File Upload Hardening

**Problem:** `ImageFileName.kt` only checks filename extension (`.jpg`/`.png`). No magic byte validation, no content-type check.

**Fix:**
- Add magic byte validation: check first bytes against known signatures (JPEG: `FF D8 FF`, PNG: `89 50 4E 47`)
- Validate `MultipartFile.contentType` matches expected image MIME types (`image/jpeg`, `image/png`)
- Sanitize filenames: strip path traversal sequences (`../`, `..\\`)
- Existing UUID-based filename generation is good and stays
- Validation happens in `PhotoService` before storing — reject invalid files with 400 response

**Files changed:**
- `value_object/ImageFileName.kt`
- `service/PhotoService.kt`
- New: `utils/FileValidationUtils.kt` (magic byte + content type checks)

---

## Sub-project 2: Configuration Hardening

### 2.1 Error Handling

**Problem:** `application.yml` lines 8-12 expose stack traces, exceptions, and binding errors to all clients.

**Fix:** Set production-safe defaults in `application.yml`:
```yaml
server:
  error:
    include-message: never
    include-stacktrace: never
    include-exception: false
    include-binding-errors: never
```
Create `application-dev.yml` with verbose settings for local development.

**Files changed:**
- `src/main/resources/application.yml`
- New: `src/main/resources/application-dev.yml`

### 2.2 Debug Logging

**Problem:** `application.yml` lines 57-60 set debug logging for Spring Web, Flyway, SpringDoc — logs all request/response details.

**Fix:** Set all to `info` or `warn` in default config. Move `debug` levels to `application-dev.yml`.

**Files changed:**
- `src/main/resources/application.yml`
- `src/main/resources/application-dev.yml`

### 2.3 Security Headers

**Problem:** No HTTP security headers configured.

**Fix:** Add a servlet `Filter` bean that sets:
- `X-Content-Type-Options: nosniff`
- `X-Frame-Options: DENY`
- `X-XSS-Protection: 0`
- `Content-Security-Policy: default-src 'self'`
- `Strict-Transport-Security: max-age=31536000; includeSubDomains`

**Files changed:**
- New: `configurations/SecurityHeadersFilter.kt`

### 2.4 CORS Configuration

**Problem:** No explicit CORS config — Spring defaults to permissive.

**Fix:** Add a `WebMvcConfigurer` bean with CORS mappings that restricts allowed origins. Use an `ALLOWED_ORIGINS` environment variable (comma-separated). If the variable is not set, no origins are allowed (empty list). Only allow `GET`, `POST`, `PATCH`, `DELETE` methods and standard headers.

**Files changed:**
- New: `configurations/CorsConfig.kt`

### 2.5 Swagger UI Protection

**Problem:** Swagger UI at root path `/` with no restriction, fully visible to anyone.

**Fix:**
- Move Swagger path from `/` to `/swagger-ui`
- Gate Swagger with `@Profile("dev")` or use `springdoc.swagger-ui.enabled` controlled by profile
- Default: disabled. Enabled in `application-dev.yml`.

**Files changed:**
- `src/main/resources/application.yml`
- `src/main/resources/application-dev.yml`

### 2.6 Default Credentials Cleanup

**Problem:** `password` as default DB password in `application.yml`. PgAdmin uses `admin@admin.com:password`.

**Fix:**
- Remove default fallback values for `DATABASE_USERNAME` and `DATABASE_PASSWORD` in `application.yml` — make them required via env vars
- Keep defaults only in `application-dev.yml`
- Update `docker-compose.yml` PgAdmin to use env vars without insecure inline defaults

**Files changed:**
- `src/main/resources/application.yml`
- `src/main/resources/application-dev.yml`
- `docker-compose.yml`

### 2.7 println Cleanup

**Problem:** Debug `println()` in `PhotoService.kt:84,93,362` and `EventCardRepository.kt:57-59` leak internal paths.

**Fix:** Remove all `println()` calls. Replace with `logger.debug()` where the information is useful, delete the rest.

**Files changed:**
- `service/PhotoService.kt`
- `repository/EventCardRepository.kt`

### 2.8 Exception Message Sanitization

**Problem:** `RestExceptionHandler.kt:56` returns `ex.rootCause` in API responses, leaking DB schema info.

**Fix:** Return generic error messages to clients ("An internal error occurred"). Log detailed errors server-side with `logger.error()`.

**Files changed:**
- `exceptions/RestExceptionHandler.kt`
- `exceptions/GlobalExceptionHandler.kt`

---

## Sub-project 3: API Hardening

### 3.1 Page Size Limits

**Problem:** `PhotoController.kt:129-163` accepts arbitrary `pageSize` — no upper bound.

**Fix:** Enforce a maximum page size of 200. Clamp values silently — if client requests more than 200, use 200. Apply in the controller (or a shared utility if multiple controllers paginate).

**Files changed:**
- `controller/PhotoController.kt`

### 3.2 Mass Assignment Prevention

**Problem:** `BaseController.patch()` binds full DTOs. Clients can set fields they shouldn't.

**Fix:**
- Audit each PatchRequestDto and remove fields that should not be client-settable
- `PhotoPatchRequestDto` — keep `securityLevel` for now (will be admin-gated once auth is added), but add a TODO comment
- `PurchaseOrderDto` — make `isCompleted` excluded from create/update input

**Files changed:**
- `dto/PurchaseOrderDto.kt` (or new `PurchaseOrderCreateDto.kt`)
- `controller/PurchaseOrderController.kt`

### 3.3 Sensitive Data Filtering

**Problem:** `SamfundetUserDto` exposes phone, email. `PurchaseOrderDto` exposes addresses. All via unauthenticated list endpoints.

**Fix:** Create slim public response DTOs:
- `SamfundetUserPublicDto` — only `id`, `firstName`, `lastName`, `profilePicturePath`
- `PurchaseOrderPublicDto` — only `id`, `isCompleted`, `sendByPost`
- List endpoints return slim DTOs. Full DTOs remain for future auth-gated detail endpoints.

**Files changed:**
- New: `dto/SamfundetUserPublicDto.kt`
- New: `dto/PurchaseOrderPublicDto.kt`
- `controller/SamfundetUserController.kt`
- `controller/PurchaseOrderController.kt`

### 3.4 Purchase Order Validation

**Problem:** `PhotoOnPurchaseOrderDto.amount` has no bounds. `isCompleted` is client-settable.

**Fix:**
- Add `@field:Min(1)` on `amount`
- Make `isCompleted` server-controlled via separate create DTO (see 3.2)

**Files changed:**
- `dto/PhotoOnPurchaseOrderDto.kt`

---

## Sub-project 4: Infrastructure

### 4.1 CI/CD Action Updates

**Problem:** `docker/build-push-action@v2.2.1` is 4+ major versions behind. `actions/setup-java@v4` in CodeQL is behind.

**Fix:**
- `docker/build-push-action@v2.2.1` → `@v6`
- `actions/setup-java@v4` → `@v5` (in `codeql-analysis.yml`)

**Files changed:**
- `.github/workflows/ci-cd.yml`
- `.github/workflows/codeql-analysis.yml`

### 4.2 Docker Compose Hardening

**Problem:** PostgreSQL 5432 exposed to host. Traefik `--api.insecure=true`.

**Fix:**
- Remove `ports: "5432:5432"` from postgres service — keep only `expose: 5432`
- Change Traefik to `--api.insecure=false`
- Add comment that PgAdmin should not be included in production deployments

**Files changed:**
- `docker-compose.yml`

### 4.3 Dependency Updates

**Problem:** `mockito-kotlin:2.2.0` is legacy (v2). `imgscalr-lib:4.2` is from 2013.

**Fix:**
- Update `mockito-kotlin` from `com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0` to `org.mockito.kotlin:mockito-kotlin:5.x` — update imports in test files accordingly
- `imgscalr-lib` — flag for future replacement but don't swap now (functional change, not a security fix)

**Files changed:**
- `pom.xml`
- Test files with mockito imports

### 4.4 Docker Image Tagging

**Problem:** CI only pushes `latest` tag — no version pinning.

**Fix:** Add git SHA tag alongside `latest`:
```yaml
tags: |
  fotogjengen/hilfling-backend:latest
  fotogjengen/hilfling-backend:${{ github.sha }}
```

**Files changed:**
- `.github/workflows/ci-cd.yml`

---

## Out of Scope

- **Authentication & Authorization (OWASP A01/A07)** — deferred to a separate project
- **IDOR fixes** — require authentication to implement properly
- **CSRF protection** — requires authentication (session/cookie-based) to be meaningful
- **Rate limiting** — deferred; can be added at reverse proxy level (Traefik) or via Spring when auth is in place
- **Audit logging** — deferred; requires user identity context from auth
- **Virus/malware scanning** — infrastructure dependency (ClamAV); flagged for future

## Implementation Order

1. Sub-project 1 (Critical Code Fixes) — highest risk, fix first
2. Sub-project 2 (Configuration Hardening) — low-risk config changes
3. Sub-project 3 (API Hardening) — DTO/controller changes
4. Sub-project 4 (Infrastructure) — CI/CD and Docker, lowest coupling

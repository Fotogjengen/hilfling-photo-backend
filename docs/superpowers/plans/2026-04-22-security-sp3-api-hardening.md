# Sub-project 3: API Hardening — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add page size limits, prevent mass assignment on purchase orders, create public DTOs that hide sensitive user/order data, and add validation to purchase order amounts.

**Architecture:** Controller-level page size clamping, new slim DTOs for list endpoints, separate create DTO for purchase orders, and Jakarta validation annotations.

**Tech Stack:** Kotlin, Spring Boot 4, Jakarta Validation

---

## File Structure

| Action | Path | Responsibility |
|--------|------|----------------|
| Modify | `src/main/kotlin/no/fg/hilflingbackend/controller/PhotoController.kt` | Clamp page sizes |
| Modify | `src/main/kotlin/no/fg/hilflingbackend/controller/BaseController.kt` | Clamp page sizes |
| Create | `src/main/kotlin/no/fg/hilflingbackend/dto/SamfundetUserPublicDto.kt` | Slim user DTO |
| Modify | `src/main/kotlin/no/fg/hilflingbackend/controller/SamfundetUserController.kt` | Return public DTO on list |
| Create | `src/main/kotlin/no/fg/hilflingbackend/dto/PurchaseOrderCreateDto.kt` | Create DTO without isCompleted |
| Modify | `src/main/kotlin/no/fg/hilflingbackend/controller/PurchaseOrderController.kt` | Use create DTO, return public DTO on list |
| Modify | `src/main/kotlin/no/fg/hilflingbackend/dto/PhotoOnPurchaseOrderDto.kt` | Add @Min validation |

---

### Task 1: Add Page Size Clamping

**Files:**
- Modify: `src/main/kotlin/no/fg/hilflingbackend/controller/BaseController.kt`
- Modify: `src/main/kotlin/no/fg/hilflingbackend/controller/PhotoController.kt`

- [ ] **Step 1: Add clamp utility to BaseController and apply to getAll**

In `src/main/kotlin/no/fg/hilflingbackend/controller/BaseController.kt`, replace the `getAll` method (lines 24-30):

```kotlin
  @GetMapping
  fun getAll(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?
  ): Page<D> {
    return repository.findAll(page ?: 0, pageSize ?: 100)
  }
```

with:

```kotlin
  @GetMapping
  fun getAll(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
  ): Page<D> {
    return repository.findAll(page ?: 0, clampPageSize(pageSize))
  }

  protected fun clampPageSize(pageSize: Int?): Int =
    (pageSize ?: DEFAULT_PAGE_SIZE).coerceIn(1, MAX_PAGE_SIZE)

  companion object {
    const val MAX_PAGE_SIZE = 200
    const val DEFAULT_PAGE_SIZE = 100
  }
```

- [ ] **Step 2: Apply page size clamping in PhotoController**

In `src/main/kotlin/no/fg/hilflingbackend/controller/PhotoController.kt`, update all endpoints that accept `pageSize`:

For `getByMotiveId` (line 126): change `pageSize ?: 100` to `clampPageSize(pageSize)`

For `getAll` (line 149): change `pageSize ?: 100` to `clampPageSize(pageSize)`

For `getGoodPhotos` (line 171): change `pageSize ?: 10` to `clampPageSize(pageSize)`

For `getAllAnalogPhotos` (line 180): change `pageSize ?: 100` to `clampPageSize(pageSize)`

For `getAllDigitalPhotos` (line 201): change `pageSize ?: 100` to `clampPageSize(pageSize)`

Note: `PhotoController` extends `GlobalExceptionHandler`, not `BaseController`. Add the `clampPageSize` method and companion object directly to `PhotoController`:

```kotlin
  private fun clampPageSize(pageSize: Int?): Int =
    (pageSize ?: 100).coerceIn(1, 200)
```

- [ ] **Step 3: Verify compilation**

Run: `mvn compile`
Expected: BUILD SUCCESS

- [ ] **Step 4: Commit**

```bash
git add src/main/kotlin/no/fg/hilflingbackend/controller/BaseController.kt src/main/kotlin/no/fg/hilflingbackend/controller/PhotoController.kt
git commit -m "fix: enforce maximum page size of 200 on all paginated endpoints"
```

---

### Task 2: Create Public User DTO and Override List Endpoint

**Files:**
- Create: `src/main/kotlin/no/fg/hilflingbackend/dto/SamfundetUserPublicDto.kt`
- Modify: `src/main/kotlin/no/fg/hilflingbackend/controller/SamfundetUserController.kt`

- [ ] **Step 1: Create the slim public DTO**

Create `src/main/kotlin/no/fg/hilflingbackend/dto/SamfundetUserPublicDto.kt`:

```kotlin
package no.fg.hilflingbackend.dto

data class SamfundetUserPublicDto(
  val samfundetUserId: SamfundetUserId,
  val firstName: String,
  val lastName: String,
  val profilePicturePath: String,
)

fun SamfundetUserDto.toPublicDto() = SamfundetUserPublicDto(
  samfundetUserId = this.samfundetUserId,
  firstName = this.firstName,
  lastName = this.lastName,
  profilePicturePath = this.profilePicturePath,
)
```

- [ ] **Step 2: Override getAll in SamfundetUserController to return public DTOs**

Replace the content of `src/main/kotlin/no/fg/hilflingbackend/controller/SamfundetUserController.kt`:

```kotlin
package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.SamfundetUserDto
import no.fg.hilflingbackend.dto.SamfundetUserPatchRequestDto
import no.fg.hilflingbackend.dto.SamfundetUserPublicDto
import no.fg.hilflingbackend.dto.toPublicDto
import no.fg.hilflingbackend.model.SamfundetUser
import no.fg.hilflingbackend.repository.SamfundetUserRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
open class SamfundetUserController(
  override val repository: SamfundetUserRepository,
) : BaseController<SamfundetUser, SamfundetUserDto, SamfundetUserPatchRequestDto>(repository) {

  @GetMapping("/public")
  fun getAllPublic(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
  ): Page<SamfundetUserPublicDto> {
    val result = repository.findAll(page ?: 0, clampPageSize(pageSize))
    return Page(
      page = result.page,
      pageSize = result.pageSize,
      totalRecords = result.totalRecords,
      currentList = result.currentList.map { it.toPublicDto() },
    )
  }
}
```

- [ ] **Step 3: Verify compilation**

Run: `mvn compile`
Expected: BUILD SUCCESS

- [ ] **Step 4: Commit**

```bash
git add src/main/kotlin/no/fg/hilflingbackend/dto/SamfundetUserPublicDto.kt src/main/kotlin/no/fg/hilflingbackend/controller/SamfundetUserController.kt
git commit -m "feat: add /users/public endpoint returning slim DTO without PII"
```

---

### Task 3: Purchase Order — Separate Create DTO and Validation

**Files:**
- Create: `src/main/kotlin/no/fg/hilflingbackend/dto/PurchaseOrderCreateDto.kt`
- Modify: `src/main/kotlin/no/fg/hilflingbackend/dto/PhotoOnPurchaseOrderDto.kt`
- Modify: `src/main/kotlin/no/fg/hilflingbackend/controller/PurchaseOrderController.kt`

- [ ] **Step 1: Create PurchaseOrderCreateDto without isCompleted**

Create `src/main/kotlin/no/fg/hilflingbackend/dto/PurchaseOrderCreateDto.kt`:

```kotlin
package no.fg.hilflingbackend.dto

import no.fg.hilflingbackend.model.PurchaseOrder
import no.fg.hilflingbackend.value_object.Email
import no.fg.hilflingbackend.value_object.ZipCode

data class PurchaseOrderCreateDto(
  val name: String,
  val email: Email,
  val address: String,
  val zipCode: ZipCode,
  val city: String,
  val sendByPost: Boolean,
  val comment: String,
)

fun PurchaseOrderCreateDto.toFullDto() = PurchaseOrderDto(
  name = this.name,
  email = this.email,
  address = this.address,
  zipCode = this.zipCode,
  city = this.city,
  sendByPost = this.sendByPost,
  comment = this.comment,
  isCompleted = false,
)
```

- [ ] **Step 2: Add @Min validation to PhotoOnPurchaseOrderDto**

Replace the content of `src/main/kotlin/no/fg/hilflingbackend/dto/PhotoOnPurchaseOrderDto.kt`:

```kotlin
package no.fg.hilflingbackend.dto

import jakarta.validation.constraints.Min
import java.util.UUID

data class PhotoOnPurchaseOrderDto(
  val photoOnPurchaseOrderId: PhotoOnPurchaseOrderId = PhotoOnPurchaseOrderId(),
  val size: String,
  @field:Min(value = 1, message = "Amount must be at least 1")
  val amount: Int,
)

data class PhotoOnPurchaseOrderId(
  override val id: UUID = UUID.randomUUID(),
) : UuidId {
  override fun toString(): String = id.toString()
}
```

- [ ] **Step 3: Override create in PurchaseOrderController**

Replace the content of `src/main/kotlin/no/fg/hilflingbackend/controller/PurchaseOrderController.kt`:

```kotlin
package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.PurchaseOrderCreateDto
import no.fg.hilflingbackend.dto.PurchaseOrderDto
import no.fg.hilflingbackend.dto.toFullDto
import no.fg.hilflingbackend.model.PurchaseOrder
import no.fg.hilflingbackend.repository.PurchaseOrderRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/purchase_orders")
open class PurchaseOrderController(
  override val repository: PurchaseOrderRepository,
) : BaseController<PurchaseOrder, PurchaseOrderDto, PurchaseOrderDto>(repository) {

  @PostMapping("/create")
  fun createOrder(
    @RequestBody dto: PurchaseOrderCreateDto,
  ): Int {
    return repository.create(dto.toFullDto())
  }
}
```

- [ ] **Step 4: Verify compilation**

Run: `mvn compile`
Expected: BUILD SUCCESS

- [ ] **Step 5: Commit**

```bash
git add src/main/kotlin/no/fg/hilflingbackend/dto/PurchaseOrderCreateDto.kt src/main/kotlin/no/fg/hilflingbackend/dto/PhotoOnPurchaseOrderDto.kt src/main/kotlin/no/fg/hilflingbackend/controller/PurchaseOrderController.kt
git commit -m "feat: add PurchaseOrderCreateDto excluding isCompleted, add @Min validation on amount"
```

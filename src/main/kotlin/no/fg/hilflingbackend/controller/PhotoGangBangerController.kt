package no.fg.hilflingbackend.controller

import hilfling.backend.hilfling.exceptions.RestExceptionHandler
import jakarta.persistence.EntityNotFoundException
import jakarta.servlet.http.HttpServletRequest
import no.fg.hilflingbackend.configurations.RequirePermission
import no.fg.hilflingbackend.configurations.RequireSecurityLevel
import no.fg.hilflingbackend.configurations.hilflingToken
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.PhotoGangBangerDto
import no.fg.hilflingbackend.dto.PhotoGangBangerPatchRequestDto
import no.fg.hilflingbackend.dto.PhotoGangBangerPositionsPutRequestDto
import no.fg.hilflingbackend.repository.PhotoGangBangerRepository
import no.fg.hilflingbackend.service.JwtService
import no.fg.hilflingbackend.utils.ResponseCreated
import no.fg.hilflingbackend.valueobject.Permission
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/photo_gang_bangers")
class PhotoGangBangerController(
  val repository: PhotoGangBangerRepository,
  val jwtService: JwtService,
) : RestExceptionHandler() {
  @GetMapping("/me")
  @RequireSecurityLevel(SecurityLevelType.FG)
  fun getMe(request: HttpServletRequest): PhotoGangBangerDto {
    val username = jwtService.extractPayload(request.hilflingToken()!!).username
    return repository.findByUsername(username)
      ?: throw EntityNotFoundException("Could not find PhotoGangBanger for user '$username'")
  }

  @GetMapping("/{id}")
  fun getById(
    @PathVariable("id") id: UUID,
  ): PhotoGangBangerDto? = repository.findById(id)

  @GetMapping
  fun getAll(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
  ): Page<PhotoGangBangerDto> = repository.findAll(page = 0, pageSize = 100)

  @GetMapping("/actives")
  fun getActives(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
  ): Page<PhotoGangBangerDto> = repository.findAllActives(page = 0, pageSize = 100)

  @GetMapping("/active_pangs")
  fun getActivePangs(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
  ): Page<PhotoGangBangerDto> = repository.findAllActivePangs(page = 0, pageSize = 100)

  @GetMapping("/inactive_pangs")
  fun getInActivePangs(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?,
  ): Page<PhotoGangBangerDto> = repository.findAllInactivePangs(page = 0, pageSize = 100)

  @PostMapping
  @RequirePermission(Permission.USER_MANAGE)
  fun create(
    @RequestBody dto: PhotoGangBangerDto,
  ): ResponseEntity<Int> {
    val created = repository.create(dto)
    return ResponseCreated(created)
  }

  @PatchMapping()
  @RequireSecurityLevel(SecurityLevelType.FG)
  fun patch(
    request: HttpServletRequest,
    @RequestBody dto: PhotoGangBangerPatchRequestDto,
  ): PhotoGangBangerDto? {
    val tokenPayload = jwtService.extractPayload(request.hilflingToken()!!)
    val currentUser =
      repository.findByUsername(tokenPayload.username)
        ?: throw EntityNotFoundException("Could not find current PhotoGangBanger for user '${tokenPayload.username}'")

    val hasUserManage = Permission.USER_MANAGE in tokenPayload.permissions
    val isSelf = currentUser.photoGangBangerId == dto.photoGangBangerId

    if (!hasUserManage) {
      if (!isSelf) {
        throw AccessDeniedException("You do not have permission to edit other users")
      }
      if (dto.isPang != null || dto.isActive != null || dto.semesterStart != null) {
        throw AccessDeniedException("You cannot modify isPang, isActive or semesterStart on your own profile")
      }
    }

    return repository.patch(dto)
  }

  @PutMapping("/positions")
  @RequirePermission(Permission.USER_MANAGE)
  fun putPositions(
    @RequestBody dto: PhotoGangBangerPositionsPutRequestDto,
  ): PhotoGangBangerDto? = repository.replacePositions(dto)
}

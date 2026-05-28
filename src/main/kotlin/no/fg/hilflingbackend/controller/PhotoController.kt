package no.fg.hilflingbackend.controller

import jakarta.servlet.http.HttpServletRequest
import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.dto.PhotoFinalizeRequestDto
import no.fg.hilflingbackend.dto.PhotoGoodPictureToggleRequestDto
import no.fg.hilflingbackend.dto.PhotoReservationDto
import no.fg.hilflingbackend.dto.PhotoUploadRequestDto
import no.fg.hilflingbackend.service.JwtService
import no.fg.hilflingbackend.service.PhotoService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/photos")
class PhotoController(
  val photoService: PhotoService,
  val jwtService: JwtService,
) {
  @GetMapping("/{id}")
  fun getById(
    @PathVariable id: UUID,
    request: HttpServletRequest,
  ): PhotoDto {
    val securityLevel = jwtService.extractSecurityLevel(request.getHeader("X-hilfling-token"))
    return photoService.findById(id, securityLevel)
  }

  @GetMapping("/motive/{motiveId}")
  fun getByMotiveId(
    @PathVariable motiveId: UUID,
    request: HttpServletRequest,
  ): List<PhotoDto> {
    val securityLevel = jwtService.extractSecurityLevel(request.getHeader("X-hilfling-token"))
    return photoService.findByMotiveId(motiveId, securityLevel)
  }

  @PostMapping("/upload/reserve")
  fun reserve(
    @RequestBody dto: PhotoUploadRequestDto,
    request: HttpServletRequest,
  ): PhotoReservationDto {
    val username = jwtService.extractPayload(request.getHeader("X-hilfling-token")).username
    return photoService.reserve(dto, username)
  }

  @PostMapping("/upload/finalize")
  fun upload(
    @RequestBody dto: PhotoFinalizeRequestDto,
    request: HttpServletRequest,
  ): PhotoDto {
    val username = jwtService.extractPayload(request.getHeader("X-hilfling-token")).username
    return photoService.upload(dto, username)
  }

  @DeleteMapping("/{id}")
  fun delete(
    @PathVariable id: UUID,
    request: HttpServletRequest,
  ): PhotoDto {
    val securityLevel = jwtService.extractSecurityLevel(request.getHeader("X-hilfling-token"))
    return photoService.delete(id, securityLevel)
  }

  @PutMapping("/{id}/good-picture")
  fun markAsGoodPicture(
    @PathVariable id: UUID,
    @RequestBody dto: PhotoGoodPictureToggleRequestDto,
    request: HttpServletRequest,
  ) {
    val payload = jwtService.extractPayload(request.getHeader("X-hilfling-token"))
    return photoService.markAsGoodPicture(id, dto.goodPicture, payload.securityLevel)
  }
}

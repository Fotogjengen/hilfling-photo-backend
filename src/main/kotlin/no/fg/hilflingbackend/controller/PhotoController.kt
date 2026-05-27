package no.fg.hilflingbackend.controller

import jakarta.servlet.http.HttpServletRequest
import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.dto.PhotoReservationDto
import no.fg.hilflingbackend.dto.PhotoUploadRequestDto
import no.fg.hilflingbackend.service.JwtService
import no.fg.hilflingbackend.service.PhotoService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
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
    val payload = jwtService.extractPayload(request.getHeader("X-hilfling-token"))
    return photoService.findById(id, payload.securityLevel)
  }

  @GetMapping("/motive/{motiveId}")
  fun getByMotiveId(
    @PathVariable motiveId: UUID,
    request: HttpServletRequest,
  ): List<PhotoDto> {
    val payload = jwtService.extractPayload(request.getHeader("X-hilfling-token"))
    return photoService.findByMotiveId(motiveId, payload.securityLevel)
  }

  @PostMapping("/upload/reserve")
  fun reserve(
    @RequestBody dto: PhotoUploadRequestDto,
  ): PhotoReservationDto = photoService.reserve(dto)

  @PostMapping("/upload/finalize")
  fun upload(
    @RequestBody dto: PhotoDto,
  ): PhotoDto = photoService.upload(dto)

  @DeleteMapping("/{id}")
  fun delete(
    @PathVariable id: UUID,
  ) = photoService.delete(id)
}

package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.SamfundetUserDto
import no.fg.hilflingbackend.dto.SamfundetUserPatchRequestDto
import no.fg.hilflingbackend.model.SamfundetUser
import no.fg.hilflingbackend.repository.SamfundetUserRepository
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
open class SamfundetUserController(
  override val repository: SamfundetUserRepository,
) : BaseController<SamfundetUser, SamfundetUserDto, SamfundetUserPatchRequestDto>(repository)

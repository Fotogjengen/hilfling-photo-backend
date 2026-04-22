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

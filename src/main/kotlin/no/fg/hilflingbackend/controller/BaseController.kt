package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.repository.IRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

open class BaseController<E, D>(open val repository: IRepository<E, D>) {

  @GetMapping("/{id}")
  fun getById(
    @PathVariable("id") id: UUID
  ): D? {
    return repository.findById(id)
  }

  @GetMapping
  fun getAll(
    @RequestParam("page", required = false) page: Int?,
    @RequestParam("pageSize", required = false) pageSize: Int?
  ): Page<D> {
    return repository.findAll(page ?: 0, pageSize ?: 100)
  }

  @PostMapping
  open fun create(
    @RequestBody dto: D
  ): Int {
    return repository.create(
      dto
    )
  }

  @PatchMapping
  fun patch(
    @RequestBody dto: D
  ): Int {
    return repository.patch(dto)
  }
}

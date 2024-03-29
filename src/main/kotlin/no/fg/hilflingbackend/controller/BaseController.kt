package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.repository.IRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

open class BaseController<E, D, R>(open val repository: IRepository<E, D, R>) {

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

  @RequestMapping("/{id}", method = [RequestMethod.DELETE])
  fun delete(
    @PathVariable("id") id: UUID
  ): Int {
    return repository.delete(id)
  }

  @PatchMapping
  fun patch(
    @RequestBody dto: R
  ): D {
    return repository.patch(dto)
  }
}

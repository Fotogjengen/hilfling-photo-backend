package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.repository.IRepository
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import java.util.*


open class BaseController<E, D>(open val repository: IRepository<E, D>) {

  @GetMapping("/{id}")
  fun getById(
    @PathVariable("id") id: UUID
  ): D? {
    return repository.findById(id)
  }

  @GetMapping
  fun getAll(
    @RequestParam("offset", required = false) offset: Int?,
    @RequestParam("limit", required = false) limit: Int?
  ): Page<D> {
    return repository.findAll(offset ?: 0, limit ?: 100)
  }

  @PostMapping
  fun create(
    @RequestBody dto: D
  ): Int {
    print(dto.toString())
    print("test")
    return repository.create(
      dto
    )
  }
}

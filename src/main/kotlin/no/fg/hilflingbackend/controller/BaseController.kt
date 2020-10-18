package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.repository.IRepository
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import java.util.*

@Component
open class Controller<T>(open val repository: IRepository<T>) {

  @GetMapping("/{id}")
  fun getById(
    @PathVariable("id") id: UUID
  ): T? {
    return repository.findById(id)
  }

  @GetMapping
  fun getAll(
    @RequestParam("offset", required = false) offset: Int?,
    @RequestParam("limit", required = false) limit: Int?
  ): Page<T> {
    return repository.findAll(offset ?: 0, limit ?: 100)
  }

  @PostMapping
  fun create(
    @RequestBody entity: T
  ): T {
    return repository.create(
      entity
    )
  }
}

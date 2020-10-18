package no.fg.hilflingbackend.repository

import no.fg.hilflingbackend.dto.Page
import org.springframework.stereotype.Component
import java.util.*

@Component
interface IRepository<T> {
  fun findById(id: UUID): T?
  fun create(entity: T): T
  fun findAll(offset: Int, limit: Int): Page<T>
  fun delete(id: UUID): Int
  fun patch(entity: T): Int
}

package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.dsl.QueryRowSet
import no.fg.hilflingbackend.dto.Page
import org.springframework.stereotype.Component
import java.util.UUID

@Component
interface IRepository<E, D> {
  fun convertToClass(qrs: QueryRowSet): D
  suspend fun findById(id: UUID): D?
  fun create(dto: D): Int
  suspend fun findAll(offset: Int, limit: Int): Page<D>
  suspend fun delete(id: UUID): Int
  fun patch(dto: D): Int
}

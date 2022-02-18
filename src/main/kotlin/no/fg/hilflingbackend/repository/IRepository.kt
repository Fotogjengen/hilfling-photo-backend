package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.dsl.QueryRowSet
import no.fg.hilflingbackend.dto.Page
import org.springframework.stereotype.Component
import java.util.UUID

@Component
interface IRepository<E, D, R> {
  fun convertToClass(qrs: QueryRowSet): D
  fun findById(id: UUID): D?
  fun create(dto: D): Int
  fun findAll(page: Int = 0, pageSize: Int = 100): Page<D>
  fun delete(id: UUID): Int
  fun patch(dto: R): D
}

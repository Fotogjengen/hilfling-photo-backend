package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.drop
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.take
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.Motive
import no.fg.hilflingbackend.model.motives
import no.fg.hilflingbackend.model.toDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
open class MotiveRepository {

  @Autowired
  open lateinit var database: Database

  // TODO: Make return MotiveDto instead of Motive
  fun findByTitle(title: String): Motive? =
    database
      .motives
      .find {
        it.title eq title
      }

  fun findById(id: UUID): MotiveDto? {
    return database.motives.find { it.id eq id }?.toDto()
  }

  fun findAll(page: Int, pageSize: Int): Page<MotiveDto> {
    val motives = database
      .motives
      .drop(page)
      .take(pageSize)
      .toList()

    val motiveDtos = motives.map { it.toDto() }

    return Page(
      page = page,
      pageSize = pageSize,
      totalRecords = database.motives.totalRecords,
      currentList = motiveDtos
    )
  }

  fun create(
    motive: MotiveDto
  ): MotiveDto {
    database.motives.add(motive.toEntity())
    return motive
  }
}

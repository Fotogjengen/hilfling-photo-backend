package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.dto.SamfundetUserDto
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.model.SamfundetUser
import no.fg.hilflingbackend.model.samfundet_users
import no.fg.hilflingbackend.model.toDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
open class SamfundetUserRepository {
  @Autowired
  open lateinit var database: Database

  fun findById(id: UUID): SamfundetUser? {
    return database.samfundet_users.find { it.id eq id }
  }

  fun findAll(): List<SamfundetUser> {
    return database.samfundet_users.toList()
  }

  fun create(
    samfundetUserDto: SamfundetUserDto
  ): SamfundetUserDto? {
    var success = 0
    try {
      success = database
        .samfundet_users
        .add(
          samfundetUserDto.toEntity()
        )
    } catch (error: Error) {
      return null
    }
    if (success == 1) {
      return findById(samfundetUserDto.samfundetUserId.id)?.toDto()
    }
    return null
  }
}

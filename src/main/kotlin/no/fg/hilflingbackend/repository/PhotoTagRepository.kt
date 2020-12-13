package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.dto.PhotoTagDto
import no.fg.hilflingbackend.model.PhotoTag
import no.fg.hilflingbackend.model.photo_tags
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
open class PhotoTagRepository {
  @Autowired
  open lateinit var database: Database

  fun findById(id: UUID): PhotoTag? {
    return database.photo_tags.find { it.id eq id }
  }

  fun findAll(): List<PhotoTag> {
    return database.photo_tags.toList()
  }

  fun create(photoTagDto: PhotoTagDto): Int {
    return database.photo_tags.add(
      PhotoTag {
        id = photoTagDto.photoTagId.id
        name = photoTagDto.name
      }
    )
  }
}

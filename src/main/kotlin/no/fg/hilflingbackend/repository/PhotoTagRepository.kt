package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.model.PhotoTag
import no.fg.hilflingbackend.model.photo_tags
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
open class PhotoTagRepository {
  @Autowired
  open lateinit var database: Database

  fun findById(id: Int): PhotoTag? {
    return database.photo_tags.find { it.id eq id }
  }

  fun findAll(): List<PhotoTag> {
    return database.photo_tags.toList()
  }

  fun createPhotoTag(photoTag: PhotoTag): PhotoTag {
    val photoTagFromDatabase = PhotoTag {
      this.name = photoTag.name
    }
    database.photo_tags.add(photoTagFromDatabase)
    return photoTagFromDatabase
  }
}

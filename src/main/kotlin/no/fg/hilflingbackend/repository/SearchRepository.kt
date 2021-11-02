package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.like
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
open class SearchRepository {

  @Autowired
  open lateinit var database: Database

  // TODO: Make return MotiveDto instead of Motive
  fun findBySearchterm(SearchTerm: String): List<Motive> =
    database
      .motives
      .filter {
        it.title like "%$SearchTerm%"
      }.toList()
}


/*
fun findAllDigitalPhotos(): List<PhotoDto> {
  return database
    .photos
    .filter {
      val motive = it.motiveId.referenceTable as Motives
      val album = motive.albumId.referenceTable as Albums
      album.isAnalog eq false
    }.toList()
    .map { it.toDto() }
}
*/

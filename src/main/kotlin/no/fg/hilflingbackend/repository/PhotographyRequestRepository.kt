package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.model.PhotographyRequest
import no.fg.hilflingbackend.model.photography_requests
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
open class PhotographyRequestRepository {
  @Autowired
  open lateinit var database: Database

  fun findById(id: UUID): PhotographyRequest? {
    return database.photography_requests.find { it.id eq id }
  }

  fun findAll(): List<PhotographyRequest> {
    return database.photography_requests.toList()
  }

  fun create(
    photographyRequest: PhotographyRequest
  ): PhotographyRequest {
    val photographyRequestFromDatabase = PhotographyRequest {
      this.startTime = photographyRequest.startTime
      this.endTime = photographyRequest.endTime
      this.place = photographyRequest.place
      this.isIntern = photographyRequest.isIntern
      this.type = photographyRequest.type
      this.name = photographyRequest.name
      this.email = photographyRequest.email
      this.phone = photographyRequest.phone
      this.description = photographyRequest.description
    }
    database.photography_requests.add(photographyRequestFromDatabase)
    return photographyRequestFromDatabase
  }
}

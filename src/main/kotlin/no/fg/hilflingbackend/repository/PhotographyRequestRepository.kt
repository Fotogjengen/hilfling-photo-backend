package no.fg.hilflingbackend.repository

import java.util.UUID
import no.fg.hilflingbackend.model.PhotographyRequest
import no.fg.hilflingbackend.model.photography_requests
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.ktorm.entity.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

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

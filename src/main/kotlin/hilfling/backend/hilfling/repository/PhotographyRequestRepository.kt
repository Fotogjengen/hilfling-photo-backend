package hilfling.backend.hilfling.repository

import hilfling.backend.hilfling.model.*
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.from
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.where
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate

class PhotographyRequestRepository {
    @Autowired
    lateinit var database: Database

    fun findById(id: Int): PhotographyRequest? {
        return database.photography_requests.find { it.id eq id }
    }

    fun findAll(): List<PhotographyRequest> {
        return database.photography_requests.toList()
    }

    fun create(
            photographyRequest: PhotographyRequest
    ): PhotographyRequest {
        val photographyRequest = PhotographyRequest{
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
        database.photography_requests.add(photographyRequest)
        return photographyRequest
    }
}
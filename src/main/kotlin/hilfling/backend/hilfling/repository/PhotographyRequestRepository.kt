package hilfling.backend.hilfing.repository

import hilfling.backend.hilfing.model.*
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

    fun findById(id: Long): PhotographyRequest? {
        return database.photography_requests.find { it.id eq id }
    }

    fun findAll(): List<PhotographyRequest> {
        return database.photography_requests.toList()
    }

    fun create(
            startTime: LocalDate,
            endTime: LocalDate,
            place: String,
            isIntern: Boolean,
            type: String,
            name: String,
            email: String,
            phone: String,
            description: String
    ): PhotographyRequest {
        val photographyRequest = PhotographyRequest{
            this.startTime = startTime
            this.endTime = endTime
            this.place = place
            this.isIntern = isIntern
            this.type = type
            this.name = name
            this.email = email
            this.phone = phone
            this.description = description
        }
        database.photography_requests.add(photographyRequest)
        return photographyRequest
    }
}
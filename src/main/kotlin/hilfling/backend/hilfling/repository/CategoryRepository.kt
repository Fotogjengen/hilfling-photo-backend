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

class CategoryRepository {
    @Autowired
    lateinit var database: Database

    fun findById(id: Int): Category? {
        return database.categories.find { it.id eq id }
    }

    fun findAll(): List<Category> {
        return database.categories.toList()
    }

    fun create(name: String): Category {
        val category = Category{
            this.name = name
        }
        database.categories.add(category)
        return category
    }
}
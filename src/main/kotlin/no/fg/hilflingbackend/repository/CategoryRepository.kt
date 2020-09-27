package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.toList
import no.fg.hilflingbackend.model.Category
import no.fg.hilflingbackend.model.categories
import org.springframework.beans.factory.annotation.Autowired

class CategoryRepository {
    @Autowired
    lateinit var database: Database

    fun findById(id: Int): Category? {
        return database.categories.find { it.id eq id }
    }

    fun findAll(): List<Category> {
        return database.categories.toList()
    }

    fun create(
            category: Category
    ): Category {
        val categoryFromDatabase = Category{
            this.name = category.name
        }
        database.categories.add(categoryFromDatabase)
        return categoryFromDatabase
    }
}
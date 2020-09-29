package no.fg.hilflingbackend.configurations

import com.fasterxml.jackson.databind.Module
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.jackson.KtormModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
open class KtormConfiguration{
    @Autowired
    lateinit var dataSource: DataSource

    @Bean
    open fun database(): Database {
        return Database.connectWithSpringSupport(dataSource)
    }
}
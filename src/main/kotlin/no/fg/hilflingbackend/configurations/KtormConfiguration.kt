package no.fg.hilflingbackend.configurations

import org.ktorm.database.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource
import tools.jackson.databind.JacksonModule

@Configuration
open class KtormConfiguration {
  @Autowired
  lateinit var dataSource: DataSource

  @Bean
  open fun database(): Database {
    return Database.connectWithSpringSupport(dataSource)
  }

}

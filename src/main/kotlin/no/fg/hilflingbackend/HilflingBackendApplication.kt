package no.fg.hilflingbackend

import no.fg.hilflingbackend.repository.AlbumRepository
import no.fg.hilflingbackend.service.PhotoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
open class HilflingBackendApplication {
	@Autowired
	lateinit var photoService: PhotoService

	@Bean
	open fun run() = CommandLineRunner {
		photoService.init()
	}
}

fun main(args: Array<String>) {
	runApplication<HilflingBackendApplication>(*args)
}

package no.fg.hilflingbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class HilflingBackendApplication

fun main(args: Array<String>) {
	runApplication<HilflingBackendApplication>(*args)
}

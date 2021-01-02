package no.fg.hilflingbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class HilflingApplication

fun main(args: Array<String>) {
  runApplication<HilflingApplication>(*args)
}

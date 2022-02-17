package no.fg.hilflingbackend.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals


/*
class HealthControllerSpec : Spek({
  describe("GET /health") {
    val healthController: HealthController = HealthController()

    assertEquals("OK!", healthController.getHealth())
  }
})
*/

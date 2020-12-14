package no.fg.hilflingbackend.controller

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

class HealthControllerSpec: Spek({
  describe("GET /health") {
    val healthController: HealthController = HealthController()

    assertEquals("OK", healthController.getHealth())

  }
})

package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.MockDataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// Only registered when seed.enabled=true (SEED_ENABLED env var). Off by default, so never exposed in prod.
@RestController
@RequestMapping("/seed")
@ConditionalOnProperty(name = ["seed.enabled"], havingValue = "true")
class SeedController {
  @Autowired
  lateinit var mockDataService: MockDataService

  @GetMapping
  fun seedMockdata(): String {
    mockDataService
      .seedMockData()
    return "Mock data seeded"
  }
}

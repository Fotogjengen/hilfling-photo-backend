package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.MockDataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Profile("dev", "test")
@RestController
@RequestMapping("/seed")
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

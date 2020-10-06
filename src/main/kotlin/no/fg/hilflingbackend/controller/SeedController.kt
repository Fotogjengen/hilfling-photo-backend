package hilfling.backend.hilfling.controller

import hilfling.backend.hilfling.MockData
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/seed")
class SeedController {
    @GetMapping
    fun Seed(): String {
        MockData()
             .seedMockData()
        return "Mock data seeded"
    }
}
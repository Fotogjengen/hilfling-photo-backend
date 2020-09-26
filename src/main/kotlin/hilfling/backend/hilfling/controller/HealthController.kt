package hilfling.backend.hilfling.controller

import org.springframework.web.bind.annotation.GetMapping

class HealthController {

    @GetMapping("/health")
    fun health() : String{
        return "Status: ok!"
    }

}
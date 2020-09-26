package hilfling.backend.hilfling.controller

import hilfling.backend.hilfling.model.PhotographyRequest
import hilfling.backend.hilfling.repository.PhotographyRequestRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/photography_requests")
class PhotographyRequestController {
    val repository = PhotographyRequestRepository()

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Int) : PhotographyRequest? {
        return repository.findById(id)
    }

    @GetMapping
    fun getAll() : List<PhotographyRequest> {
        return repository.findAll()
    }

    @PostMapping
    fun create(
            @RequestParam("photographyRequest") photographyRequest: PhotographyRequest
    ): PhotographyRequest {
        return repository.create(photographyRequest)
    }
}
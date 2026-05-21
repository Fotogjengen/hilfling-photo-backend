package no.fg.hilflingbackend.controller

import jakarta.persistence.EntityNotFoundException
import jakarta.servlet.http.HttpServletRequest
import no.fg.hilflingbackend.dto.EventCardDto
import no.fg.hilflingbackend.dto.EventOwnerName
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.exceptions.GlobalExceptionHandler
import no.fg.hilflingbackend.repository.EventCardRepository
import no.fg.hilflingbackend.repository.EventOwnerRepository
import no.fg.hilflingbackend.service.JwtService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/eventcards")
class EventCardController(
  val eventCardRepository: EventCardRepository,
  val eventOwnerRepository: EventOwnerRepository,
  val jwtService: JwtService,
) : GlobalExceptionHandler() {

  private fun allowedSecurityLevels(request: HttpServletRequest): List<String> =
    jwtService.allowedSecurityLevels(request.getHeader("X-hilfling-token"))

  @GetMapping("/latest")
  fun getNLatestEventCardsOfType(
    request: HttpServletRequest,
    @RequestParam("eventOwnerName") eventOwnerName: String,
    @RequestParam("numberOfEventCards") numberOfEventCards: Int,
  ): List<EventCardDto> {
    val eventOwnerFromDb =
      eventOwnerRepository.findByEventOwnerName(
        EventOwnerName.valueOf(eventOwnerName),
      )
        ?: throw EntityNotFoundException("Did not find eventOwner")

    return eventCardRepository.getLatestEventCards(
      numberOfEventCards = numberOfEventCards,
      eventOwner = eventOwnerFromDb.toEntity(),
      allowedSecurityLevels = allowedSecurityLevels(request),
    )
  }

  @GetMapping("/search")
  fun searchEventCardsGlobal(
    request: HttpServletRequest,
    @RequestParam("searchString", required = false) searchString: String?,
    @RequestParam("page") page: Int = 0,
    @RequestParam("pageSize") pageSize: Int = 10,
  ): Page<EventCardDto> =
    eventCardRepository.searchEventCards(
      searchTerm = searchString ?: "",
      page = page,
      pageSize = pageSize,
      allowedSecurityLevels = allowedSecurityLevels(request),
    )
}

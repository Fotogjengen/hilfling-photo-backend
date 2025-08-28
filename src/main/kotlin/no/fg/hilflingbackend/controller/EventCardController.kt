package no.fg.hilflingbackend.controller

import jakarta.persistence.EntityNotFoundException
import no.fg.hilflingbackend.dto.EventCardDto
import no.fg.hilflingbackend.dto.EventOwnerName
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.exceptions.GlobalExceptionHandler
import no.fg.hilflingbackend.repository.EventCardRepository
import no.fg.hilflingbackend.repository.EventOwnerRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/eventcards")
class EventCardController(
  val eventCardRepository: EventCardRepository,
  val eventOwnerRepository: EventOwnerRepository,
) : GlobalExceptionHandler() {
  @GetMapping("/latest")
  fun getNLatestEventCardsOfType(
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
    )
  }

  @GetMapping("/search")
  fun searchEventCardsGlobal(
    @RequestParam("searchString", required = false) searchString: String?,
    @RequestParam("page") page: Int = 0,
    @RequestParam("pageSize") pageSize: Int = 10,
  ): Page<EventCardDto> =
    eventCardRepository.searchEventCards(
      searchTerm = searchString ?: "",
      page = page,
      pageSize = pageSize,
    )
}

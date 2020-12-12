package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.EventCardDto
import no.fg.hilflingbackend.dto.EventOwnerName
import no.fg.hilflingbackend.dto.toEntity
import no.fg.hilflingbackend.exceptions.GlobalExceptionHandler
import no.fg.hilflingbackend.repository.EventCardRepository
import no.fg.hilflingbackend.repository.EventOwnerRepository
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException
import javax.management.BadAttributeValueExpException

@RestController
@RequestMapping("/eventcards")
class EventCardController(
  val eventCardRepository: EventCardRepository,
  val eventOwnerRepository: EventOwnerRepository
): GlobalExceptionHandler() {

  @GetMapping()
  fun getNLatestEventCardsOfType(
    @RequestParam("eventOwnerName") eventOwnerName: String,
    @RequestParam("number_of_eventcards") numberOfEventCards: Int
  ): List<EventCardDto> {

    val eventOwnerFromDb = eventOwnerRepository.findByType(
      EventOwnerName.valueOf(eventOwnerName)) ?: throw BadAttributeValueExpException("Not found eventOwner")

    return eventCardRepository.getLatestEventCards(
      numberOfEventCards = numberOfEventCards,
      eventOwner = eventOwnerFromDb.toEntity()
    )
  }
}

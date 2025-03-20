package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.map
import no.fg.hilflingbackend.dto.EventCardDto
import no.fg.hilflingbackend.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
open class EventCardRepository {
  @Autowired
  open lateinit var database: Database

  fun getLatestEventCards(
    eventOwner: EventOwner,
    numberOfEventCards: Int,
  ): List<EventCardDto> =
    database
      .from(Motives)
      .innerJoin(EventOwners, on = Motives.eventOwnerId eq EventOwners.id)
      .select(
        Motives.id,
        Motives.title,
        Motives.dateCreated,
        EventOwners.name,
      ).where { EventOwners.name eq eventOwner.name }
      .limit(0, numberOfEventCards)
      .map { row ->
        EventCardDto(
          motiveId = row[Motives.id],
          motiveTitle = row[Motives.title],
          date_created = row[Motives.dateCreated],
          eventOwnerName = row[EventOwners.name],
          frontPageSmallPhotoUrl =
            database
              .photos
              .filter {
                it.motiveId eq Motives.id
                it.isGoodPicture eq true
              }.map { it.smallUrl }
              .first(),
        )
      }.toList()
}

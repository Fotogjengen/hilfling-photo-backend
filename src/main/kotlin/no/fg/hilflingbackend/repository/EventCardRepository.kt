package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.*
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
        val motiveId = row[Motives.id]
        val selectedPhotoUrl =
          motiveId?.let { id ->
            database
              .from(Photos)
              .select(Photos.smallUrl)
              .where { (Photos.motiveId eq id) and (Photos.isGoodPicture eq true) }
              .limit(0, 1) // Only fetch one record
              .map { it[Photos.smallUrl] }
              .firstOrNull()
          }

        println("Motive ID: $motiveId → Selected Small URL: $selectedPhotoUrl") // Debugging
        EventCardDto(
          motiveId = motiveId,
          motiveTitle = row[Motives.title],
          date_created = row[Motives.dateCreated],
          eventOwnerName = row[EventOwners.name],
          frontPageSmallPhotoUrl = selectedPhotoUrl,
        )
      }
}

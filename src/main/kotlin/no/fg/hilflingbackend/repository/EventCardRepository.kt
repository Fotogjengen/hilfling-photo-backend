package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.desc
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.from
import me.liuwj.ktorm.dsl.innerJoin
import me.liuwj.ktorm.dsl.isNotNull
import me.liuwj.ktorm.dsl.limit
import me.liuwj.ktorm.dsl.map
import me.liuwj.ktorm.dsl.orderBy
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.where
import me.liuwj.ktorm.support.postgresql.ilike
import no.fg.hilflingbackend.dto.EventCardDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.model.EventOwner
import no.fg.hilflingbackend.model.EventOwners
import no.fg.hilflingbackend.model.Motives
import no.fg.hilflingbackend.model.Photos
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
open class EventCardRepository {
  @Autowired open lateinit var database: Database

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
              .where {
                (Photos.motiveId eq id).and(Photos.isGoodPicture eq true)
              }.limit(0, 1) // Only fetch one record
              .map { it[Photos.smallUrl] }
              .firstOrNull()
          }

        println(
          "Motive ID: $motiveId → Selected Small URL: $selectedPhotoUrl",
        ) // Debugging
        EventCardDto(
          motiveId = motiveId,
          motiveTitle = row[Motives.title],
          date_created = row[Motives.dateCreated],
          eventOwnerName = row[EventOwners.name],
          frontPageSmallPhotoUrl = selectedPhotoUrl,
        )
      }

  // Search event cards by term without specifying the event owner
  fun searchEventCards(
    searchTerm: String,
    page: Int = 0,
    pageSize: Int = 10,
  ): Page<EventCardDto> {
    val eventCards =
      database
        .from(Motives)
        .innerJoin(EventOwners, on = Motives.eventOwnerId eq EventOwners.id)
        .select(
          Motives.id,
          Motives.title,
          Motives.dateCreated,
          EventOwners.name,
        ).where {
          if (searchTerm.isBlank()) {
            // Return all results, ordered by latest first
            Motives.id.isNotNull()
          } else {
            Motives.title ilike "%$searchTerm%"
          }
        }.orderBy(Motives.dateCreated.desc())
        .limit(page * pageSize, pageSize)
        .map { row ->
          val motiveId = row[Motives.id]
          val selectedPhotoUrl =
            motiveId?.let { id ->
              database
                .from(Photos)
                .select(Photos.smallUrl)
                .where {
                  (Photos.motiveId eq id).and(Photos.isGoodPicture eq true)
                }.limit(0, 1)
                .map { it[Photos.smallUrl] }
                .firstOrNull()
            }

          EventCardDto(
            motiveId = motiveId,
            motiveTitle = row[Motives.title],
            date_created = row[Motives.dateCreated],
            eventOwnerName = row[EventOwners.name],
            frontPageSmallPhotoUrl = selectedPhotoUrl,
          )
        }
    return Page(
      page = page,
      pageSize = pageSize,
      totalRecords = eventCards.size,
      currentList = eventCards,
    )
  }
}

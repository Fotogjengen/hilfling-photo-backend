package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.crossJoin
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.from
import me.liuwj.ktorm.dsl.limit
import me.liuwj.ktorm.dsl.map
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.where
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.map
import no.fg.hilflingbackend.dto.EventCardDto
import no.fg.hilflingbackend.model.EventOwner
import no.fg.hilflingbackend.model.EventOwners
import no.fg.hilflingbackend.model.Motives
import no.fg.hilflingbackend.model.Places
import no.fg.hilflingbackend.model.photos
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
open class EventCardRepository {
  @Autowired
  open lateinit var database: Database

  fun getLatestEventCards(
    numberOfEventCards: Int,
    eventOwner: EventOwner,
  ): List<EventCardDto> =
    database
      .from(Motives)
      .crossJoin(Places)
      .crossJoin(EventOwners)
      .select(
        Motives.title,
        Motives.title,
        Motives.dateCreated,
        Places.name,
        EventOwners.name,
      ).where { EventOwners.name eq eventOwner.name }
      .limit(0, 6)
      .map { row ->
        EventCardDto(
          motiveTitle = row[Motives.title],
          date_crated = row[Motives.dateCreated],
          locationTaken = row[Places.name],
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

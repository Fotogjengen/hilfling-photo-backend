package no.fg.hilflingbackend.model

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf
import no.fg.hilflingbackend.dto.SearchDto

interface Search : BaseModel<Search> {
  companion object : Entity.Factory<Search>()

  var suggestions: List<String>
}

fun Search.toDto(): SearchDto = SearchDto(
  suggestions = this.suggestions,
)

val Database.searches get() = this.sequenceOf(Motives)

package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.ActiveFilters
import no.fg.hilflingbackend.dto.FilterSuggestionsResponseDto
import no.fg.hilflingbackend.service.SearchSuggestionsService
import no.fg.hilflingbackend.utils.ResponseOk
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.util.UUID

@RestController
@RequestMapping("/search/suggestions")
class SearchSuggestionsController(
  val service: SearchSuggestionsService,
) {
  @GetMapping
  fun getSearchSuggestions(
    @RequestParam(required = false, defaultValue = "") q: String,
    @RequestParam(required = false) placeIds: List<UUID>?,
    @RequestParam(required = false) categoryIds: List<UUID>?,
    @RequestParam(required = false) eventOwnerIds: List<UUID>?,
    @RequestParam(required = false) albumIds: List<UUID>?,
    @RequestParam(required = false) securityLevels: List<String>?,
    @RequestParam(required = false) from: LocalDate?,
    @RequestParam(required = false) to: LocalDate?,
  ): ResponseEntity<FilterSuggestionsResponseDto> =
    ResponseOk(
      service.findSuggestions(
        term = q,
        userLevel = resolveUserLevel(),
        filters =
          ActiveFilters(
            placeIds = placeIds ?: emptyList(),
            categoryIds = categoryIds ?: emptyList(),
            eventOwnerIds = eventOwnerIds ?: emptyList(),
            albumIds = albumIds ?: emptyList(),
            securityLevels = securityLevels ?: emptyList(),
            dateFrom = from,
            dateTo = to,
          ),
      ),
    )

  private fun resolveUserLevel(): SecurityLevelType {
    val authorities = SecurityContextHolder.getContext().authentication?.authorities
    return when {
      authorities?.any { it.authority == "ROLE_FG" } == true -> SecurityLevelType.FG
      authorities?.any { it.authority == "ROLE_HUSFOLK" } == true -> SecurityLevelType.HUSFOLK
      else -> SecurityLevelType.ALLE
    }
  }
}

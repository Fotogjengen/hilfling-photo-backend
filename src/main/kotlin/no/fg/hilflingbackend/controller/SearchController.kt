package no.fg.hilflingbackend.controller

import no.fg.hilflingbackend.dto.ActiveFilters
import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.service.SearchService
import no.fg.hilflingbackend.utils.ResponseOk
import no.fg.hilflingbackend.valueobject.SearchSortField
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import no.fg.hilflingbackend.valueobject.SortDirection
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.util.UUID

@RestController
@RequestMapping("/search")
class SearchController(
  val service: SearchService,
) {
  @GetMapping("/motives")
  fun searchMotives(
    @RequestParam(required = false, defaultValue = "") q: String,
    @RequestParam(required = false) placeIds: List<UUID>?,
    @RequestParam(required = false) categoryIds: List<UUID>?,
    @RequestParam(required = false) eventOwnerIds: List<UUID>?,
    @RequestParam(required = false) albumIds: List<UUID>?,
    @RequestParam(required = false) securityLevels: List<String>?,
    @RequestParam(required = false) from: LocalDate?,
    @RequestParam(required = false) to: LocalDate?,
    @RequestParam(required = false) sortField: SearchSortField?,
    @RequestParam(required = false, defaultValue = "DESC") sortDirection: SortDirection,
    @RequestParam(required = false, defaultValue = "0") page: Int,
    @RequestParam(required = false, defaultValue = "10") pageSize: Int,
  ): ResponseEntity<Page<MotiveDto>> =
    ResponseOk(
      service.searchMotives(
        term = q,
        userLevel = resolveUserLevel(),
        filters = buildFilters(placeIds, categoryIds, eventOwnerIds, albumIds, securityLevels, from, to),
        sortField = resolveSortField(sortField, q),
        sortDirection = sortDirection,
        page = page,
        pageSize = pageSize,
      ),
    )

  @GetMapping("/pictures")
  fun searchPictures(
    @RequestParam(required = false, defaultValue = "") q: String,
    @RequestParam(required = false) placeIds: List<UUID>?,
    @RequestParam(required = false) categoryIds: List<UUID>?,
    @RequestParam(required = false) eventOwnerIds: List<UUID>?,
    @RequestParam(required = false) albumIds: List<UUID>?,
    @RequestParam(required = false) securityLevels: List<String>?,
    @RequestParam(required = false) from: LocalDate?,
    @RequestParam(required = false) to: LocalDate?,
    @RequestParam(required = false) sortField: SearchSortField?,
    @RequestParam(required = false, defaultValue = "DESC") sortDirection: SortDirection,
    @RequestParam(required = false, defaultValue = "0") page: Int,
    @RequestParam(required = false, defaultValue = "10") pageSize: Int,
  ): ResponseEntity<Page<PhotoDto>> =
    ResponseOk(
      service.searchPictures(
        term = q,
        userLevel = resolveUserLevel(),
        filters = buildFilters(placeIds, categoryIds, eventOwnerIds, albumIds, securityLevels, from, to),
        sortField = resolveSortField(sortField, q),
        sortDirection = sortDirection,
        page = page,
        pageSize = pageSize,
      ),
    )

  private fun buildFilters(
    placeIds: List<UUID>?,
    categoryIds: List<UUID>?,
    eventOwnerIds: List<UUID>?,
    albumIds: List<UUID>?,
    securityLevels: List<String>?,
    from: LocalDate?,
    to: LocalDate?,
  ): ActiveFilters =
    ActiveFilters(
      placeIds = placeIds ?: emptyList(),
      categoryIds = categoryIds ?: emptyList(),
      eventOwnerIds = eventOwnerIds ?: emptyList(),
      albumIds = albumIds ?: emptyList(),
      securityLevels = securityLevels ?: emptyList(),
      dateFrom = from,
      dateTo = to,
    )

  // Default to relevance ranking while searching; fall back to newest-first when browsing without a term.
  private fun resolveSortField(
    sortField: SearchSortField?,
    term: String,
  ): SearchSortField = sortField ?: if (term.isNotBlank()) SearchSortField.RELEVANCE else SearchSortField.DATE_TAKEN

  private fun resolveUserLevel(): SecurityLevelType {
    val authorities = SecurityContextHolder.getContext().authentication?.authorities
    return when {
      authorities?.any { it.authority == "ROLE_FG" } == true -> SecurityLevelType.FG
      authorities?.any { it.authority == "ROLE_HUSFOLK" } == true -> SecurityLevelType.HUSFOLK
      else -> SecurityLevelType.ALLE
    }
  }
}

package no.fg.hilflingbackend.service

import no.fg.hilflingbackend.dto.ActiveFilters
import no.fg.hilflingbackend.dto.MotiveDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.dto.PhotoDto
import no.fg.hilflingbackend.repository.SearchRepository
import no.fg.hilflingbackend.valueobject.SearchSortField
import no.fg.hilflingbackend.valueobject.SecurityLevelType
import no.fg.hilflingbackend.valueobject.SortDirection
import org.springframework.stereotype.Service

@Service
class SearchService(
  val repository: SearchRepository,
) {
  fun searchMotives(
    term: String,
    userLevel: SecurityLevelType,
    filters: ActiveFilters,
    sortField: SearchSortField,
    sortDirection: SortDirection,
    page: Int,
    pageSize: Int,
  ): Page<MotiveDto> = repository.searchMotives(term, userLevel, filters, sortField, sortDirection, page, pageSize)

  fun searchPictures(
    term: String,
    userLevel: SecurityLevelType,
    filters: ActiveFilters,
    sortField: SearchSortField,
    sortDirection: SortDirection,
    page: Int,
    pageSize: Int,
  ): Page<PhotoDto> = repository.searchPictures(term, userLevel, filters, sortField, sortDirection, page, pageSize)
}

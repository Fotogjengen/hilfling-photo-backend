package no.fg.hilflingbackend.dto

data class Page<T> (
  val offset: Int,
  val limit: Int,
  val totalRecords: Int,
  val pageSize: Int = limit,
  val totalPages: Int = if (totalRecords % pageSize == 0) totalRecords / pageSize else totalRecords / pageSize + 1,
  val currentList: List<T>
)

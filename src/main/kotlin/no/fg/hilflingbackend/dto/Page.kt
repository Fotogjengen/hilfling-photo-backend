package no.fg.hilflingbackend.dto

data class Page<T>(
  val totalRecords: Int,
  val pageSize: Int,
  val page: Int,
  val totalPages: Int = if (totalRecords % pageSize == 0) totalRecords / pageSize else totalRecords / pageSize + 1,
  val currentList: List<T>
) {
  companion object {
    fun <T> empty(
      page: Int = 0,
      pageSize: Int = 1,
    ): Page<T> =
      Page(
        totalRecords = 0,
        pageSize = pageSize,
        page = page,
        totalPages = 0,
        currentList = emptyList(),
      )
  }
}

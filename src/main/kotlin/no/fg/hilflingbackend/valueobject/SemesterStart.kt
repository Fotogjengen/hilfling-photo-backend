package no.fg.hilflingbackend.valueobject

import java.time.LocalDate

data class SemesterStart private constructor(
  val value: String,
) : Comparable<SemesterStart> {
  override fun compareTo(other: SemesterStart): Int = sortValue().compareTo(other.sortValue())

  private fun sortValue(): Int {
    // Two-digit years up to next year are 20xx, older ones 19xx
    val twoDigit = value.substring(1).toInt()
    val year = if (twoDigit <= (LocalDate.now().year + 1) % 100) 2000 + twoDigit else 1900 + twoDigit
    return year * 2 + if (value.startsWith("H")) 1 else 0
  }

  companion object {
    private val SEMESTER_PATTERN = Regex("^[VH]\\d{2}$")

    operator fun invoke(value: String): SemesterStart {
      val normalized = value.trim().uppercase()
      if (!isValidSemesterStart(normalized)) {
        throw IllegalArgumentException("Invalid semester start value: '$value'")
      }
      return SemesterStart(normalized)
    }

    fun current(): SemesterStart {
      val today = LocalDate.now()
      val season = if (today.monthValue <= 6) "V" else "H"
      return invoke("%s%02d".format(season, today.year % 100))
    }

    fun isValidSemesterStart(semesterStart: String): Boolean = SEMESTER_PATTERN.matches(semesterStart)
  }
}

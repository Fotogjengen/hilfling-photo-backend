package no.fg.hilflingbackend.valueobject

import java.time.LocalDate

data class SemesterStart private constructor(
  val value: String,
) {
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

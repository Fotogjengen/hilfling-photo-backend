package no.fg.hilflingbackend.valueobject

import java.time.LocalDate

data class SemesterStart private constructor(
  val value: String,
) {
  companion object {
    operator fun invoke(value: String): SemesterStart =
      if (isValidSemesterStart(value)) {
        SemesterStart(value)
      } else {
        throw IllegalArgumentException("Invalid semester start value: '$value'")
      }

    fun current(): SemesterStart {
      val today = LocalDate.now()
      return SemesterStart("${if (today.monthValue <= 6) "V" else "H"}${today.year}")
    }

    fun isValidSemesterStart(semesterStart: String): Boolean {
      // TODO: Implement
      return true
    }
  }
}

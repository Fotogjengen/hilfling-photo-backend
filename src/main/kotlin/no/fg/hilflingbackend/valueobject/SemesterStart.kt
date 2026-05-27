package no.fg.hilflingbackend.valueobject

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

    fun isValidSemesterStart(semesterStart: String): Boolean {
      // TODO: Implement
      return true
    }
  }
}

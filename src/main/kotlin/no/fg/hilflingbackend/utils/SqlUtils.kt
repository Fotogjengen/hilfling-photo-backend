package no.fg.hilflingbackend.utils

fun escapeLikePattern(input: String): String =
  input
    .replace("\\", "\\\\")
    .replace("%", "\\%")
    .replace("_", "\\_")

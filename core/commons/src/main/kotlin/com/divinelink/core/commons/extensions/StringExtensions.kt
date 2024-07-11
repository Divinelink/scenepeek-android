package com.divinelink.core.commons.extensions

import java.text.SimpleDateFormat
import java.util.Locale

fun String.formatTo(
  inputFormat: String,
  outputFormat: String,
): String? {
  val input = SimpleDateFormat(inputFormat, Locale.ENGLISH)
  val output = SimpleDateFormat(outputFormat, Locale.ENGLISH)
  val date = input.parse(this)
  return date?.let { output.format(it) }
}

fun String?.extractDetailsFromDeepLink(): Pair<Int, String>? {
  // Example URL format: "https://www.themoviedb.org/tv/693134-dune-part-two"
  return this?.let {
    val segments = it.split("/")
    if (segments.size >= 4) {
      val mediaType = segments[3]
      val id = segments[4].substringBefore("-").toIntOrNull()
      id?.let { safeId ->
        return Pair(safeId, mediaType)
      }
    }
    return null
  }
}

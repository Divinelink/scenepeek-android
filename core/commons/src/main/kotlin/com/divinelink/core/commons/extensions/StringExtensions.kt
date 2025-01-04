package com.divinelink.core.commons.extensions

import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
import java.util.Locale

fun String.formatTo(
  inputFormat: String,
  outputFormat: String,
): String? = try {
  val input = SimpleDateFormat(inputFormat, Locale.ENGLISH)
  val output = SimpleDateFormat(outputFormat, Locale.ENGLISH)
  val date = input.parse(this)
  date?.let { output.format(it) }
} catch (e: Exception) {
  null
}

fun String?.extractDetailsFromDeepLink(): Pair<Int, String>? {
  // Example URL format: "https://www.themoviedb.org/tv/693134-dune-part-two"
  return this?.let {
    val segments = it.split("/")
    if (segments.size in 4..5) {
      val mediaType = segments[3]
      val id = segments[4].substringBefore("-").toIntOrNull()
      id?.let { safeId ->
        return Pair(safeId, mediaType)
      }
    }
    return null
  }
}

fun calculateAge(
  fromDate: String,
  toDate: String? = null,
  clock: Clock = Clock.System,
): Int {
  // Parse the birthday string into a LocalDate object
  val birthDate = LocalDate.parse(fromDate)
  // Get the current date or the the date of death in the specified timezone
  val currentDate = if (toDate == null) {
    clock.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
  } else {
    LocalDate.parse(toDate)
  }

  // Calculate the age by finding the difference in years
  var age = currentDate.year - birthDate.year

  // Check if the birthday has not occurred yet this year, subtract one year if so
  if (currentDate < birthDate.plus(DatePeriod(years = age))) {
    age--
  }

  return age
}

fun String.calculateFourteenDayRange(clock: Clock = Clock.System): List<Pair<String, String>> {
  val startInstant = Instant.fromEpochSeconds(this.toLong())

  val startDate = startInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date
  val currentDate = clock.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

  val periods = mutableListOf<Pair<String, String>>()

  var periodStartDate = startDate

  while (periodStartDate <= currentDate) {
    // Calculate the end date for the current period (14 days after the start date)
    val periodEndDate = (periodStartDate + DatePeriod(days = 14)).coerceAtMost(currentDate)

    periods.add(
      Pair(
        periodStartDate.toString(),
        periodEndDate.toString(),
      ),
    )

    periodStartDate = periodEndDate + DatePeriod(days = 1)
  }

  return periods
}

/**
 * Check if the string in format "yyyy-MM-dd" is today's date
 */
fun String.isDateToday(clock: Clock = Clock.System): Boolean =
  this == clock.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()

/**
 * Check if timestamp is today's date
 */
fun String.isInstantToday(clock: Clock = Clock.System): Boolean {
  val instant = Instant.fromEpochSeconds(this.toLong())
  val date = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date

  return date == clock.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
}

fun String.isValidEmail(): Boolean {
  val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
  return this.matches(emailRegex.toRegex())
}

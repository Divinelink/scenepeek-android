package com.divinelink.core.commons.extensions

import com.divinelink.core.commons.provider.ClockProvider
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.parseOrNull
import kotlinx.datetime.toInstant
import kotlinx.datetime.todayIn
import kotlin.time.Clock

fun LocalDate.Companion.parseOrNull(input: CharSequence?) = LocalDate
  .parseOrNull(input ?: "")

fun LocalDate.toEpochSeconds(timeZone: TimeZone): Long = this
  .atStartOfDayIn(timeZone)
  .epochSeconds

fun LocalDate.isBeforeNowUtc(clock: Clock): Boolean = this < clock.todayIn(TimeZone.UTC)

fun LocalDate.isAfterNowUtc(clock: Clock): Boolean = this > clock.todayIn(TimeZone.UTC)

fun LocalDate.daysFromToday(clock: ClockProvider): Int {
  val today = clock.now.dayOfYear
  return dayOfYear - today
}

fun LocalDate.isAfterNow(clock: ClockProvider): Boolean = compareTo(clock.now) > 0

fun LocalDateTime?.isBeforeNowMinus(
  clock: ClockProvider,
  days: Int = 0,
  months: Int = 0,
  years: Int = 0,
  hours: Int = 0,
  minutes: Int = 0,
  seconds: Int = 0,
): Boolean {
  this ?: return true
  val timeZone = clock.timezone
  val now = clock.clock.now()
  val thisInstant = this.toInstant(timeZone)

  var pastInstant = now
  if (years != 0) pastInstant = pastInstant.minus(years, DateTimeUnit.YEAR, timeZone)
  if (months != 0) pastInstant = pastInstant.minus(months, DateTimeUnit.MONTH, timeZone)
  if (days != 0) pastInstant = pastInstant.minus(days, DateTimeUnit.DAY, timeZone)
  if (hours != 0) pastInstant = pastInstant.minus(hours, DateTimeUnit.HOUR, timeZone)
  if (minutes != 0) pastInstant = pastInstant.minus(minutes, DateTimeUnit.MINUTE, timeZone)
  if (seconds != 0) pastInstant = pastInstant.minus(seconds, DateTimeUnit.SECOND, timeZone)

  return thisInstant <= pastInstant
}

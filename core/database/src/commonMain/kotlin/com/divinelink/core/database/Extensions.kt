package com.divinelink.core.database

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

const val CACHE_DURATION_IN_MONTHS = 1

fun Clock.cacheExpiresAtToEpochSeconds(): String {
  val systemTimeZone = TimeZone.currentSystemDefault()
  return now().plus(
    CACHE_DURATION_IN_MONTHS,
    DateTimeUnit.MONTH,
    systemTimeZone,
  ).epochSeconds.toString()
}

fun Clock.currentEpochSeconds(): String = now().epochSeconds.toString()

fun Clock.currentTimeInUTC(): String {
  val utcTimeZone = TimeZone.UTC
  val currentInstant = now()
  val utcDateTime = currentInstant.toLocalDateTime(utcTimeZone)

  // Format as "yyyy-MM-dd HH:mm:ss UTC"
  val year = utcDateTime.year
  val month = utcDateTime.month.number.toString().padStart(2, '0')
  val day = utcDateTime.day.toString().padStart(2, '0')
  val hour = utcDateTime.hour.toString().padStart(2, '0')
  val minute = utcDateTime.minute.toString().padStart(2, '0')
  val second = utcDateTime.second.toString().padStart(2, '0')

  return "$year-$month-$day $hour:$minute:$second UTC"
}

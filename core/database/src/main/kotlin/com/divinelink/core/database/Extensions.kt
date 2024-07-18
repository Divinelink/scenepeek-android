package com.divinelink.core.database

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus

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

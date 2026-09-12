package com.divinelink.core.commons.extensions

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.parseOrNull
import kotlinx.datetime.todayIn
import kotlin.time.Clock

fun LocalDate.Companion.parseOrNull(input: CharSequence?) = LocalDate
  .parseOrNull(input ?: "")

fun LocalDate.isBeforeNowUtc(clock: Clock): Boolean = this < clock.todayIn(TimeZone.UTC)

fun LocalDate.isAfterNowUtc(clock: Clock): Boolean = this > clock.todayIn(TimeZone.UTC)

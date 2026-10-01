package com.divinelink.core.commons.provider

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock

interface ClockProvider {
  val clock: Clock
  val timezone: TimeZone

  val now: LocalDate
    get() = clock.todayIn(timezone)

  val nowDT: LocalDateTime
    get() = clock.now().toLocalDateTime(timezone)
}

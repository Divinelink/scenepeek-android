package com.divinelink.core.fixtures.core.commons

import kotlin.time.Clock
import kotlin.time.Instant

object ClockFactory {

  // GMT: Sunday, 1 August 2021 08:00:00
  fun augustFirst2021(): Clock = object : Clock {
    override fun now(): Instant = Instant.fromEpochSeconds(1627804800)
  }

  // GMT: Sunday, 15 August 2021 02:40:00
  fun augustFifteenth2021(): Clock = object : Clock {
    override fun now(): Instant = Instant.fromEpochSeconds(1628995200)
  }

  // GMT: Sunday, 28 August 2021 08:00:00
  fun augustTwentyEighth2021(): Clock = object : Clock {
    override fun now(): Instant = Instant.fromEpochSeconds(1630137600)
  }

  // GMT: Thursday, 2 September 2021 08:00:00
  fun septemberSecond2021(): Clock = object : Clock {
    override fun now(): Instant = Instant.fromEpochSeconds(1630569600)
  }

  fun decemberFirst2021(): Clock = object : Clock {
    override fun now(): Instant = Instant.fromEpochSeconds(1638345600)
  }
}

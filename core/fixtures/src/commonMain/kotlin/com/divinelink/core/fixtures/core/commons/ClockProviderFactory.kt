package com.divinelink.core.fixtures.core.commons

import com.divinelink.core.commons.provider.ClockProvider
import kotlinx.datetime.TimeZone
import kotlin.time.Clock

object ClockProviderFactory {

  // GMT: Sunday, 1 August 2021 08:00:00
  fun augustFirst2021(): ClockProvider = object : ClockProvider {
    override val clock: Clock = ClockFactory.augustFirst2021()
    override val timezone: TimeZone = TimeZone.UTC
  }

  // GMT: Sunday, 15 August 2021 02:40:00
  fun augustFifteenth2021(): ClockProvider = object : ClockProvider {
    override val clock: Clock = ClockFactory.augustFifteenth2021()
    override val timezone: TimeZone = TimeZone.UTC
  }

  // GMT: Sunday, 28 August 2021 08:00:00
  fun augustTwentyEighth2021(): ClockProvider = object : ClockProvider {
    override val clock: Clock = ClockFactory.augustTwentyEighth2021()
    override val timezone: TimeZone = TimeZone.UTC
  }

  // GMT: Thursday, 2 September 2021 08:00:00
  fun septemberSecond2021(): ClockProvider = object : ClockProvider {
    override val clock: Clock = ClockFactory.septemberSecond2021()
    override val timezone: TimeZone = TimeZone.UTC
  }

  fun decemberFirst2021(): ClockProvider = object : ClockProvider {
    override val clock: Clock = ClockFactory.decemberFirst2021()
    override val timezone: TimeZone = TimeZone.UTC
  }

  fun maySecond2022(): ClockProvider = object : ClockProvider {
    override val clock: Clock = ClockFactory.maySecond2022()
    override val timezone: TimeZone = TimeZone.UTC
  }
}

package com.divinelink.core.fixtures.core.commons

import com.divinelink.core.commons.provider.ClockProvider
import kotlinx.datetime.TimeZone
import kotlin.time.Clock

class PreviewClockProvider : ClockProvider {
  override val clock: Clock = ClockFactory.augustTwentyEighth2021()
  override val timezone: TimeZone = TimeZone.UTC
}

package com.divinelink.core.testing.factories.database.credits

import com.divinelink.core.database.credits.AggregateCredits
import com.divinelink.core.testing.factories.core.commons.DateFactory

object AggregateCreditsFactory {

  fun theOffice() = AggregateCredits(
    id = 2316,
    expiresAtEpochSeconds = DateFactory.SEPTEMBER_SECOND_2021,
  )
}

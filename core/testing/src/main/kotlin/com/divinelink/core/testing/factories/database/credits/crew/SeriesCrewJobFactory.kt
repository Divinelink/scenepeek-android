package com.divinelink.core.testing.factories.database.credits.crew

import com.divinelink.core.database.credits.crew.SeriesCrewJob
import com.divinelink.core.testing.factories.database.credits.AggregateCreditsFactory

object SeriesCrewJobFactory {

  fun daleAlexander() = SeriesCrewJob(
    creditId = "5bdaa7d90e0a2603c60086d9",
    job = "Key Grip",
    episodeCount = 3,
    crewId = 1879373,
    aggregateCreditId = AggregateCreditsFactory.theOffice().id,
  )

  fun randallEinhorn() = SeriesCrewJob(
    creditId = "5bdaa68f92514153f500859f",
    job = "Director of Photography",
    episodeCount = 3,
    crewId = 1215572,
    aggregateCreditId = AggregateCreditsFactory.theOffice().id,
  )

  fun ronNichols() = SeriesCrewJob(
    creditId = "5bdaa3e40e0a2603b1008d3f",
    job = "Key Grip",
    episodeCount = 1,
    crewId = 2166021,
    aggregateCreditId = AggregateCreditsFactory.theOffice().id,
  )

  fun peterSmokler() = SeriesCrewJob(
    creditId = "5bdaa2d4c3a368078f007f5c",
    job = "Director of Photography",
    episodeCount = 1,
    crewId = 67864,
    aggregateCreditId = AggregateCreditsFactory.theOffice().id,
  )

  fun allCrewJobs() = listOf(
    daleAlexander(),
    peterSmokler(),
    randallEinhorn(),
    ronNichols(),
  )
}

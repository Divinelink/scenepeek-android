package com.divinelink.core.testing.factories.entity.credits

import com.divinelink.core.database.credits.crew.SeriesCrewJob
import com.divinelink.core.database.credits.model.CrewEntity
import com.divinelink.core.testing.factories.database.credits.AggregateCreditsFactory

object CrewEntityFactory {

  fun randallEinhorn() = CrewEntity(
    id = 1215572,
    name = "Randall Einhorn",
    originalName = "Randall Einhorn",
    profilePath = null,
    department = "Camera",
    gender = 2,
    knownForDepartment = "Directing",
    roles = listOf(
      SeriesCrewJob(
        creditId = "5bdaa68f92514153f500859f",
        job = "Director of Photography",
        episodeCount = 3,
        crewId = 1215572,
        department = "Camera",
        aggregateCreditId = AggregateCreditsFactory.theOffice().id,
      ),
    ),
  )

  fun daleAlexander() = CrewEntity(
    id = 1879373,
    name = "Dale Alexander",
    originalName = "Dale Alexander",
    profilePath = null,
    department = "Camera",
    knownForDepartment = "Camera",
    gender = 0,
    roles = listOf(
      SeriesCrewJob(
        creditId = "5bdaa7d90e0a2603c60086d9",
        job = "Key Grip",
        episodeCount = 3,
        crewId = 1879373,
        department = "Camera",
        aggregateCreditId = AggregateCreditsFactory.theOffice().id,
      ),
    ),
  )

  fun ronNichols() = CrewEntity(
    id = 2166021,
    name = "Ron Nichols",
    originalName = "Ron Nichols",
    profilePath = null,
    department = "Camera",
    knownForDepartment = "Camera",
    gender = 0,
    roles = listOf(
      SeriesCrewJob(
        creditId = "5bdaa3e40e0a2603b1008d3f",
        job = "Key Grip",
        episodeCount = 1,
        crewId = 2166021,
        department = "Camera",
        aggregateCreditId = AggregateCreditsFactory.theOffice().id,
      ),
    ),
  )

  fun peterSmokler() = CrewEntity(
    id = 67864,
    name = "Peter Smokler",
    originalName = "Peter Smokler",
    profilePath = null,
    department = "Camera",
    knownForDepartment = "Camera",
    gender = 2,
    roles = listOf(
      SeriesCrewJob(
        creditId = "5bdaa2d4c3a368078f007f5c",
        job = "Director of Photography",
        episodeCount = 1,
        crewId = 67864,
        department = "Camera",
        aggregateCreditId = AggregateCreditsFactory.theOffice().id,
      ),
    ),
  )

  fun cameraDepartment() = listOf(
    daleAlexander(),
    peterSmokler(),
    randallEinhorn(),
    ronNichols(),
  )
}

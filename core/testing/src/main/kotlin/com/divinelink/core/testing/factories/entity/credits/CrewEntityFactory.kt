package com.divinelink.core.testing.factories.entity.credits

import com.divinelink.core.database.credits.model.CrewEntity
import com.divinelink.core.testing.factories.database.credits.AggregateCreditsFactory

object CrewEntityFactory {

  fun randallEinhorn() = CrewEntity(
    id = 1215572,
    name = "Randall Einhorn",
    originalName = "Randall Einhorn",
    job = "Director of Photography",
    profilePath = null,
    totalEpisodeCount = 3,
    aggregateCreditId = AggregateCreditsFactory.theOffice().id,
    department = "Camera",
    gender = 2,
    knownForDepartment = "Directing",
    creditId = "5bdaa68f92514153f500859f",
  )

  fun daleAlexander() = CrewEntity(
    id = 1879373,
    name = "Dale Alexander",
    originalName = "Dale Alexander",
    job = "Key Grip",
    profilePath = null,
    totalEpisodeCount = 3,
    aggregateCreditId = AggregateCreditsFactory.theOffice().id,
    department = "Camera",
    knownForDepartment = "Camera",
    gender = 0,
    creditId = "5bdaa7d90e0a2603c60086d9",
  )

  fun ronNichols() = CrewEntity(
    id = 2166021,
    name = "Ron Nichols",
    originalName = "Ron Nichols",
    job = "Key Grip",
    profilePath = null,
    totalEpisodeCount = 1,
    aggregateCreditId = AggregateCreditsFactory.theOffice().id,
    department = "Camera",
    knownForDepartment = "Camera",
    gender = 0,
    creditId = "5bdaa3e40e0a2603b1008d3f",
  )

  fun peterSmokler() = CrewEntity(
    id = 67864,
    name = "Peter Smokler",
    originalName = "Peter Smokler",
    job = "Director of Photography",
    profilePath = null,
    totalEpisodeCount = 1,
    aggregateCreditId = AggregateCreditsFactory.theOffice().id,
    department = "Camera",
    knownForDepartment = "Camera",
    gender = 2,
    creditId = "5bdaa2d4c3a368078f007f5c",
  )

  fun cameraDepartment() = listOf(
    daleAlexander(),
    peterSmokler(),
    randallEinhorn(),
    ronNichols(),
  )
}

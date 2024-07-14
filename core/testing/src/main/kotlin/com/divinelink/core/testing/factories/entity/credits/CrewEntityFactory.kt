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
  )

  fun cameraDepartment() = listOf(
    daleAlexander(),
    randallEinhorn(),
    ronNichols(),
    peterSmokler(),
  )
}

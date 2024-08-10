package com.divinelink.core.data.details.mapper.api

import com.divinelink.core.database.credits.cast.SeriesCast
import com.divinelink.core.database.credits.cast.SeriesCastRole
import com.divinelink.core.database.credits.crew.SeriesCrew
import com.divinelink.core.database.credits.crew.SeriesCrewJob
import com.divinelink.core.network.media.model.credits.AggregateCreditsApi

fun AggregateCreditsApi.toSeriesCastEntity() = cast.map { cast ->
  SeriesCast(
    id = cast.id,
    name = cast.name,
    profilePath = cast.profilePath,
    originalName = cast.name,
    totalEpisodeCount = cast.totalEpisodeCount.toLong(),
    knownForDepartment = cast.knownForDepartment,
    aggregateCreditId = id,
  )
}

fun AggregateCreditsApi.toSeriesCastRoleEntity() = cast
  .filter { it.roles.isNotEmpty() }
  .map { cast ->
    SeriesCastRole(
      aggregateCreditId = id,
      creditId = cast.roles.first().creditId,
      character = cast.roles.first().character,
      episodeCount = cast.totalEpisodeCount.toLong(),
      castId = cast.id,
    )
  }

fun AggregateCreditsApi.toSeriesCrewEntity(): List<SeriesCrew> = crew
  .filter { it.jobs.isNotEmpty() }
  .map { crew ->
    SeriesCrew(
      id = crew.id,
      name = crew.name,
      profilePath = crew.profilePath,
      department = crew.department,
      job = crew.jobs.first().job,
      aggregateCreditId = id,
      originalName = crew.originalName,
      totalEpisodeCount = crew.totalEpisodeCount,
      knownForDepartment = crew.knownForDepartment,
    )
  }

fun AggregateCreditsApi.toSeriesCrewJobEntity() = crew
  .filter { it.jobs.isNotEmpty() }
  .map { crew ->
    SeriesCrewJob(
      aggregateCreditId = id,
      job = crew.jobs.first().job,
      creditId = crew.jobs.first().creditId,
      episodeCount = crew.totalEpisodeCount,
      crewId = crew.id,
    )
  }

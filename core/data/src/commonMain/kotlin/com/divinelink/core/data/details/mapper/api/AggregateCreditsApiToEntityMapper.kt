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
    gender = cast.gender,
    aggregateCreditId = id,
  )
}

fun AggregateCreditsApi.toSeriesCastRoleEntity() = cast
  .filter { it.roles.isNotEmpty() }
  .flatMap { cast ->
    cast.roles.map { role ->
      SeriesCastRole(
        aggregateCreditId = id,
        creditId = role.creditId,
        character = role.character,
        episodeCount = role.episodeCount.toLong(),
        castId = cast.id,
      )
    }
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
      gender = crew.gender.toLong(),
    )
  }

fun AggregateCreditsApi.toSeriesCrewJobEntity() = crew
  .filter { it.jobs.isNotEmpty() }
  .flatMap { crew ->
    crew.jobs.map { role ->
      SeriesCrewJob(
        aggregateCreditId = id,
        creditId = role.creditId,
        job = role.job,
        episodeCount = role.episodeCount.toLong(),
        crewId = crew.id,
        department = crew.department,
      )
    }
  }

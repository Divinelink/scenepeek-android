package com.divinelink.core.data.details.mapper.api

import com.divinelink.core.database.cast.PersonEntity
import com.divinelink.core.database.cast.PersonRoleEntity
import com.divinelink.core.database.credits.ShowCastRoleEntity
import com.divinelink.core.database.credits.crew.SeriesCrew
import com.divinelink.core.database.credits.crew.SeriesCrewJob
import com.divinelink.core.network.media.model.credits.AggregateCreditsApi

fun AggregateCreditsApi.toPersonsEntity() = cast.map { cast ->
  PersonEntity(
    id = cast.id,
    name = cast.name,
    profilePath = cast.profilePath,
    originalName = cast.name,
    knownForDepartment = cast.knownForDepartment,
    gender = cast.gender,
  )
}

fun AggregateCreditsApi.toRolesEntity(): List<Pair<PersonRoleEntity, ShowCastRoleEntity>> = cast
  .filter { it.roles.isNotEmpty() }
  .flatMap { cast ->
    cast.roles.map { role ->
      PersonRoleEntity(
        creditId = role.creditId,
        character = role.character,
        castId = cast.id,
      ) to ShowCastRoleEntity(
        showId = id,
        creditId = role.creditId,
        episodeCount = role.episodeCount.toLong(),
        creditOrder = cast.order.toLong(),
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

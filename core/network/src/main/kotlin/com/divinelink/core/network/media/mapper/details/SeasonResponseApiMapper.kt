package com.divinelink.core.network.media.mapper.details

import com.divinelink.core.model.details.Season
import com.divinelink.core.network.media.model.details.season.SeasonResponseApi

fun List<SeasonResponseApi>.map(): List<Season> = mapNotNull { it.map() }

fun SeasonResponseApi.map(): Season? {
  if (airDate == null) return null

  return Season(
    id = id,
    name = name,
    overview = overview,
    airDate = airDate,
    episodeCount = episodeCount,
    posterPath = posterPath,
    seasonNumber = seasonNumber,
    voteAverage = voteAverage,
  )
}

package com.divinelink.core.data.details.mapper

import com.divinelink.core.model.credits.SeriesCast
import com.divinelink.core.network.media.model.credits.SeriesCastApi

fun List<SeriesCastApi>.map() = map { it.map() }

fun SeriesCastApi.map() = SeriesCast(
  id = id,
  adult = adult,
  name = name,
  totalEpisodes = totalEpisodeCount,
  character = this.roles.firstOrNull()?.character,
  profilePath = profilePath,
)

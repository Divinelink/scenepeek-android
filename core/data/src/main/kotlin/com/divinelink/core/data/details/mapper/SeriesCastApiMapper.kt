package com.divinelink.core.data.details.mapper

import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.network.media.model.credits.SeriesCastApi

fun List<SeriesCastApi>.map() = map { it.map() }

fun SeriesCastApi.map() = Person(
  id = id,
  name = name,
  profilePath = profilePath,
  role = PersonRole.SeriesActor(
    character = this.roles.firstOrNull()?.character,
    totalEpisodes = totalEpisodeCount,
  ),
)

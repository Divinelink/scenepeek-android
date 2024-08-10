package com.divinelink.core.data.details.mapper.api

import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.network.media.model.credits.SeriesCastApi

fun List<SeriesCastApi>.map() = map { it.map() }

fun SeriesCastApi.map() = Person(
  id = id,
  name = name,
  profilePath = profilePath,
  knownForDepartment = knownForDepartment,
  role = PersonRole.SeriesActor(
    character = this.roles.firstOrNull()?.character,
    creditId = this.roles.firstOrNull()?.creditId,
    totalEpisodes = totalEpisodeCount,
  ),
)

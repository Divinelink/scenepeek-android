package com.divinelink.core.data.details.mapper.api

import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.person.Gender
import com.divinelink.core.network.media.model.credits.RolesApi
import com.divinelink.core.network.media.model.credits.SeriesCastApi

fun List<SeriesCastApi>.map() = map { it.map() }

fun SeriesCastApi.map() = Person(
  id = id,
  name = name,
  profilePath = profilePath,
  gender = Gender.from(gender.toInt()),
  knownForDepartment = knownForDepartment,
  role = roles.mapRoles(),
)

private fun List<RolesApi>.mapRoles(): List<PersonRole.SeriesActor> = map { role ->
  PersonRole.SeriesActor(
    character = role.character,
    creditId = role.creditId,
    totalEpisodes = role.episodeCount,
  )
}

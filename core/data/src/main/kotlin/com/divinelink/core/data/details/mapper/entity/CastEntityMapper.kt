package com.divinelink.core.data.details.mapper.entity

import com.divinelink.core.database.credits.model.CastEntity
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.person.Gender

fun List<CastEntity>.map() = map { it.map() }

fun CastEntity.map() = Person(
  id = id,
  name = name,
  profilePath = profilePath,
  knownForDepartment = knownForDepartment,
  gender = Gender.from(gender.toInt()),
  role = roles.map {
    PersonRole.SeriesActor(
      character = it.character,
      totalEpisodes = it.episodeCount?.toInt(),
      creditId = it.creditId,
    )
  },
)

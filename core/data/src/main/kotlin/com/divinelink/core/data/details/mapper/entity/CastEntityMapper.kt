package com.divinelink.core.data.details.mapper.entity

import com.divinelink.core.database.credits.model.CastEntity
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person

fun List<CastEntity>.map() = map { it.map() }

fun CastEntity.map() = Person(
  id = id,
  name = name,
  profilePath = profilePath,
  role = PersonRole.SeriesActor(
    character = this.character,
    totalEpisodes = totalEpisodeCount.toInt(),
  ),
)

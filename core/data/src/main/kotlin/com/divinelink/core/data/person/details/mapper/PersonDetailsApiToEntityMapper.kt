package com.divinelink.core.data.person.details.mapper

import com.divinelink.core.database.person.PersonEntity
import com.divinelink.core.network.details.person.model.PersonDetailsApi

fun PersonDetailsApi.mapToEntity(timestamp: String) = PersonEntity(
  id = id,
  biography = biography,
  birthday = birthday,
  deathday = deathday,
  gender = gender.toLong(),
  homepage = homepage,
  imdbId = imdbId,
  knownForDepartment = knownForDepartment,
  name = name,
  placeOfBirth = placeOfBirth,
  popularity = popularity,
  profilePath = profilePath,
  insertedAt = timestamp,
)

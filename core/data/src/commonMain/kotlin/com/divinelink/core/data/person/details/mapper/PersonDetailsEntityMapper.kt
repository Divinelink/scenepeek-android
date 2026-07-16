package com.divinelink.core.data.person.details.mapper

import com.divinelink.core.database.person.PersonDetailsEntity
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.person.Gender

fun PersonDetailsEntity.map(
  saved: Boolean,
) = PersonDetails(
  person = MediaItem.Person(
    id = id,
    name = name,
    profilePath = profilePath,
    gender = Gender.from(gender.toInt()),
    knownForDepartment = knownForDepartment,
    role = listOf(PersonRole.Unknown),
    saved = saved,
  ),
  biography = biography,
  birthday = birthday,
  deathday = deathday,
  placeOfBirth = placeOfBirth,
  homepage = homepage,
  alsoKnownAs = emptyList(),
  imdbId = imdbId,
  popularity = popularity,
  insertedAt = insertedAt,
)

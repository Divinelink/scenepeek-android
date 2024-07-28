package com.divinelink.core.data.details.person.mapper

import com.divinelink.core.database.person.PersonEntity
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.model.person.Gender

fun PersonEntity.map() = PersonDetails(
  person = Person(
    id = id,
    name = name,
    profilePath = profilePath,
    gender = Gender.from(gender.toInt()),
    role = PersonRole.Unknown, // TODO Implement
  ),
  biography = biography,
  birthday = birthday,
  deathday = deathday,
  placeOfBirth = placeOfBirth,
  homepage = homepage,
  alsoKnownAs = emptyList(), // TODO Implement
  imdbId = imdbId,
  popularity = popularity,
)

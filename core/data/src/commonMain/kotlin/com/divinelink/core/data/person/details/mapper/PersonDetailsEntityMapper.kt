package com.divinelink.core.data.person.details.mapper

import com.divinelink.core.database.person.PersonDetailsEntity
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.model.person.Gender

fun PersonDetailsEntity.map() = PersonDetails(
  person = Person(
    id = id,
    name = name,
    profilePath = profilePath,
    gender = Gender.from(gender.toInt()),
    knownForDepartment = knownForDepartment,
    role = listOf(PersonRole.Unknown),
  ),
  biography = biography,
  birthday = birthday,
  deathday = deathday,
  placeOfBirth = placeOfBirth,
  homepage = homepage,
  alsoKnownAs = emptyList(), // TODO Implement
  imdbId = imdbId,
  popularity = popularity,
  insertedAt = insertedAt,
)

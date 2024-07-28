package com.divinelink.core.data.details.person.mapper

import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.model.person.Gender
import com.divinelink.core.network.details.person.model.PersonDetailsApi

fun PersonDetailsApi.map() = PersonDetails(
  person = Person(
    id = id,
    name = name,
    profilePath = profilePath,
    gender = Gender.from(gender),
    role = PersonRole.Unknown, // TODO Implement
  ),
  biography = biography,
  birthday = birthday,
  deathday = deathday,
  placeOfBirth = placeOfBirth,
  homepage = homepage,
  alsoKnownAs = alsoKnownAs,
  imdbId = imdbId,
  popularity = popularity,
)

package com.divinelink.core.testing.factories.entity.person

import com.divinelink.core.database.person.PersonEntity
import com.divinelink.core.fixtures.details.person.PersonDetailsFactory
import com.divinelink.core.model.person.Gender

object PersonEntityFactory {

  fun empty() = PersonEntity(
    id = 4495,
    biography = null,
    birthday = null,
    deathday = null,
    gender = Gender.NOT_SET.value.toLong(),
    homepage = null,
    imdbId = null,
    knownForDepartment = null,
    name = "",
    placeOfBirth = null,
    popularity = 0.0,
    profilePath = null,
    insertedAt = "1628995200",
  )

  fun steveCarell(): PersonEntity = PersonEntity(
    id = 4495,
    biography = PersonDetailsFactory.BIOGRAPHY,
    birthday = "1962-08-16",
    deathday = null,
    gender = 2,
    homepage = null,
    imdbId = "nm0136797",
    knownForDepartment = "Acting",
    name = "Steve Carell",
    placeOfBirth = "Concord, Massachusetts, USA",
    popularity = 77.108,
    profilePath = "/dzJtsLspH5Bf8Tvw7OQC47ETNfJ.jpg",
    insertedAt = "1628995200", // GMT: Sunday, 15 August 2021 02:40:00
  )
}

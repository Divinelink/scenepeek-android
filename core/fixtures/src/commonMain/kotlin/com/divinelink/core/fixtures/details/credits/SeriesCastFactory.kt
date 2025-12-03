package com.divinelink.core.fixtures.details.credits

import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.person.Gender

object SeriesCastFactory {

  fun cast() = listOf(
    brianBaumgartner(),
    angelaKinsey(),
    oscarNunez(),
    kateFlannery(),
    creedBratton(),
    leslieDavidBaker(),
    phyllisSmith(),
    rainnWilson(),
  )

  fun brianBaumgartner() = Person(
    id = 94622,
    name = "Brian Baumgartner",
    role = listOf(
      PersonRole.SeriesActor(
        character = "Kevin Malone",
        totalEpisodes = 217,
        creditId = "525730a9760ee3776a3447f1",
      ),
    ),
    gender = Gender.MALE,
    knownForDepartment = "Acting",
    profilePath = "/1O7ECkD4mOKAgMAbQADBpTKBzOP.jpg",
  )

  fun angelaKinsey() = Person(
    id = 113867,
    name = "Angela Kinsey",
    role = listOf(
      PersonRole.SeriesActor(
        character = "Angela Martin",
        totalEpisodes = 210,
        creditId = "525730ab760ee3776a344a0b",
      ),
    ),
    gender = Gender.FEMALE,
    knownForDepartment = "Acting",
    profilePath = "/qHFidnMcFqUWdMFyjmXufBlYckd.jpg",
  )

  fun oscarNunez() = Person(
    id = 76094,
    name = "Óscar Núñez",
    role = listOf(
      PersonRole.SeriesActor(
        character = "Óscar Martínez",
        totalEpisodes = 203,
        creditId = "525730ab760ee3776a3449d5",
      ),
    ),
    gender = Gender.MALE,
    knownForDepartment = "Acting",
    profilePath = "/UBILHiRphJdlshvsyH920QSAhk.jpg",
  )

  fun kateFlannery() = Person(
    id = 304282,
    name = "Kate Flannery",
    role = listOf(
      PersonRole.SeriesActor(
        character = "Meredith Palmer",
        totalEpisodes = 202,
        creditId = "525730ac760ee3776a344bfb",
      ),
    ),
    gender = Gender.FEMALE,
    knownForDepartment = "Acting",
    profilePath = "/wFXWKB2IUyB6Cu08PyovyBAm9WT.jpg",
  )

  fun creedBratton() = Person(
    id = 85177,
    name = "Creed Bratton",
    role = listOf(
      PersonRole.SeriesActor(
        character = "Creed Bratton",
        totalEpisodes = 194,
        creditId = "525730ac760ee3776a344bc5",
      ),
    ),
    gender = Gender.MALE,
    knownForDepartment = "Acting",
    profilePath = "/72hNnta4igAn2cE6fDyKElHcZ09.jpg",
  )

  fun leslieDavidBaker() = Person(
    id = 1230842,
    name = "Leslie David Baker",
    role = listOf(
      PersonRole.SeriesActor(
        character = "Stanley Hudson",
        totalEpisodes = 193,
        creditId = "525730ab760ee3776a344a87",
      ),
    ),
    gender = Gender.MALE,
    knownForDepartment = "Acting",
    profilePath = "/9h3xlV5IYqKinlQCW1ouU7sjwWF.jpg",
  )

  fun phyllisSmith() = Person(
    id = 169200,
    name = "Phyllis Smith",
    role = listOf(
      PersonRole.SeriesActor(
        character = "Phyllis Lapin",
        totalEpisodes = 190,
        creditId = "525730ab760ee3776a344b03",
      ),
    ),
    gender = Gender.FEMALE,
    knownForDepartment = "Acting",
    profilePath = "/h9w9pQbiderRWAC2mi7spjzuIGz.jpg",
  )

  fun rainnWilson() = Person(
    id = 11678,
    name = "Rainn Wilson",
    role = listOf(
      PersonRole.SeriesActor(
        character = "Dwight Schrute",
        totalEpisodes = 188,
        creditId = "525730a9760ee3776a34474f",
      ),
    ),
    gender = Gender.MALE,
    knownForDepartment = "Acting",
    profilePath = "/rEDRAFYX5n2JKJh9EKILX42klA5.jpg",
  )
}

package com.divinelink.core.testing.factories.details.person

import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.person.Gender

object PersonFactory {

  object Camera {

    fun randallEinhorn() = Person(
      id = 1215572,
      name = "Randall Einhorn",
      profilePath = null,
      knownForDepartment = "Directing",
      gender = Gender.MALE,
      role = PersonRole.Crew(
        job = "Director of Photography",
        creditId = "5bdaa68f92514153f500859f",
        totalEpisodes = 3,
        department = "Camera",
      ),
    )

    fun daleAlexander() = Person(
      id = 1879373,
      name = "Dale Alexander",
      profilePath = null,
      knownForDepartment = "Camera",
      gender = Gender.NOT_SET,
      role = PersonRole.Crew(
        job = "Key Grip",
        creditId = "5bdaa7d90e0a2603c60086d9",
        totalEpisodes = 3,
        department = "Camera",
      ),
    )

    fun ronNichols() = Person(
      id = 2166021,
      name = "Ron Nichols",
      profilePath = null,
      knownForDepartment = "Camera",
      gender = Gender.NOT_SET,
      role = PersonRole.Crew(
        job = "Key Grip",
        creditId = "5bdaa3e40e0a2603b1008d3f",
        totalEpisodes = 1,
        department = "Camera",
      ),
    )

    fun peterSmokler() = Person(
      id = 67864,
      name = "Peter Smokler",
      profilePath = null,
      gender = Gender.MALE,
      knownForDepartment = "Camera",
      role = PersonRole.Crew(
        job = "Director of Photography",
        creditId = "5bdaa2d4c3a368078f007f5c",
        totalEpisodes = 1,
        department = "Camera",
      ),
    )
  }

  object Art {

    fun steveRostine() = Person(
      id = 2166017,
      name = "Steve Rostine",
      profilePath = null,
      knownForDepartment = "Art",
      gender = Gender.NOT_SET,
      role = PersonRole.Crew(
        job = "Set Decoration",
        creditId = "5bdaa3990e0a2603b40089a6",
        totalEpisodes = 4,
        department = "Art",
      ),
    )

    fun philipDShea() = Person(
      id = 1844757,
      name = "Philip D. Shea",
      profilePath = null,
      knownForDepartment = "Art",
      gender = Gender.MALE,
      role = PersonRole.Crew(
        job = "Property Master",
        creditId = "5bdaa766c3a36807820082a8",
        totalEpisodes = 3,
        department = "Art",
      ),
    )

    fun donaldLeeHarris() = Person(
      id = 33562,
      name = "Donald Lee Harris",
      profilePath = null,
      knownForDepartment = "Art",
      role = PersonRole.Crew(
        job = "Production Design",
        creditId = "5bdaafa2c3a368078b008d77",
        totalEpisodes = 1,
        department = "Art",
      ),
    )

    fun melodyMelton() = Person(
      id = 2166018,
      name = "Melody Melton",
      profilePath = null,
      knownForDepartment = "Art",
      role = PersonRole.Crew(
        job = "Property Master",
        creditId = "5bdaa3a29251415407007e49",
        totalEpisodes = 1,
        department = "Art",
      ),
    )
  }
}

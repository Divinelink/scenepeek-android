package com.divinelink.core.testing.factories.details.credits

import com.divinelink.core.model.credits.AggregateCredits
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.credits.SeriesCrewDepartment
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.person.Gender
import com.divinelink.core.testing.factories.details.person.PersonFactory

object AggregatedCreditsFactory {

  fun credits() = AggregateCredits(
    id = 2316,
    cast = SeriesCastFactory.cast(),
    crewDepartments = SeriesCrewListFactory.crewDepartments(),
  )

  // Data fetched from the API is unsorted
  fun unsortedCredits() = AggregateCredits(
    id = 2316,
    cast = SeriesCastFactory.cast().take(2),
    crewDepartments = listOf(
      SeriesCrewListFactory.unsortedCameraDepartment(),
    ),
  )

  // Data fetched from the database is sorted by name
  fun partialCredits() = AggregateCredits(
    id = 2316,
    cast = SeriesCastFactory.cast().take(2),
    crewDepartments = listOf(SeriesCrewListFactory.sortedCameraDepartment()),
  )
}

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

object SeriesCrewListFactory {

  fun crewDepartments() = listOf(
    art(),
    camera(),
    costumeAndMakeUp(),
  )

  fun art() = SeriesCrewDepartment(
    department = "Art",
    crewList = listOf(
      PersonFactory.Art.steveRostine(),
      PersonFactory.Art.philipDShea(),
      PersonFactory.Art.donaldLeeHarris(),
      PersonFactory.Art.melodyMelton(),
    ),
  )

  fun unsortedCameraDepartment() = SeriesCrewDepartment(
    department = "Camera",
    crewList = listOf(
      PersonFactory.Camera.randallEinhorn(),
      PersonFactory.Camera.daleAlexander(),
      PersonFactory.Camera.ronNichols(),
      PersonFactory.Camera.peterSmokler(),
    ),
  )

  fun camera() = SeriesCrewDepartment(
    department = "Camera",
    crewList = listOf(
      PersonFactory.Camera.randallEinhorn(),
      PersonFactory.Camera.daleAlexander(),
      PersonFactory.Camera.ronNichols(),
      PersonFactory.Camera.peterSmokler(),
    ),
  )

  fun sortedCameraDepartment() = SeriesCrewDepartment(
    department = "Camera",
    crewList = listOf(
      PersonFactory.Camera.daleAlexander(),
      PersonFactory.Camera.peterSmokler(),
      PersonFactory.Camera.randallEinhorn(),
      PersonFactory.Camera.ronNichols(),
    ),
  )

  fun costumeAndMakeUp() = SeriesCrewDepartment(
    department = "Costume & Make-Up",
    crewList = listOf(
      Person(
        id = 1325655,
        name = "Elinor Bardach",
        profilePath = null,
        knownForDepartment = "Costume & Make-Up",
        role = listOf(
          PersonRole.Crew(
            job = "Costume Supervisor",
            creditId = "5bdaa36e92514153fb008795",
            totalEpisodes = 4,
            department = "Costume & Make-Up",
          ),
        ),
      ),
      Person(
        id = 2166015,
        name = "Carey Bennett",
        profilePath = null,
        knownForDepartment = "Costume & Make-Up",
        role = listOf(
          PersonRole.Crew(
            job = "Costume Designer",
            creditId = "5bdaa3650e0a2603bf008174",
            totalEpisodes = 4,
            department = "Costume & Make-Up",
          ),
        ),
      ),
      Person(
        id = 1664353,
        name = "Cyndra Dunn",
        profilePath = null,
        knownForDepartment = "Costume & Make-Up",
        role = listOf(
          PersonRole.Crew(
            job = "Key Hair Stylist",
            creditId = "5bdaa74d0e0a2603b4008cbf",
            totalEpisodes = 3,
            department = "Costume & Make-Up",
          ),
        ),
      ),
      Person(
        id = 1543004,
        name = "Maria Valdivia",
        profilePath = null,
        knownForDepartment = "Costume & Make-Up",
        role = listOf(
          PersonRole.Crew(
            job = "Key Hair Stylist",
            creditId = "5bdaa38cc3a3680772009017",
            totalEpisodes = 1,
            department = "Costume & Make-Up",
          ),
        ),
      ),
    ),
  )
}

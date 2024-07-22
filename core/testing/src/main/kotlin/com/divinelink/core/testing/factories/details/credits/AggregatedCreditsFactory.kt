package com.divinelink.core.testing.factories.details.credits

import com.divinelink.core.model.credits.AggregateCredits
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.credits.SeriesCrewDepartment
import com.divinelink.core.model.details.Person

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
    role = PersonRole.SeriesActor(
      character = "Kevin Malone",
      totalEpisodes = 217,
      creditId = "525730a9760ee3776a3447f1",
    ),
    profilePath = "/1O7ECkD4mOKAgMAbQADBpTKBzOP.jpg",
  )

  fun angelaKinsey() = Person(
    id = 113867,
    name = "Angela Kinsey",
    role = PersonRole.SeriesActor(
      character = "Angela Martin",
      totalEpisodes = 210,
      creditId = "525730ab760ee3776a344a0b",
    ),
    profilePath = "/qHFidnMcFqUWdMFyjmXufBlYckd.jpg",
  )

  fun oscarNunez() = Person(
    id = 76094,
    name = "Óscar Núñez",
    role = PersonRole.SeriesActor(
      character = "Óscar Martínez",
      totalEpisodes = 203,
      creditId = "525730ab760ee3776a3449d5",
    ),
    profilePath = "/UBILHiRphJdlshvsyH920QSAhk.jpg",
  )

  fun kateFlannery() = Person(
    id = 304282,
    name = "Kate Flannery",
    role = PersonRole.SeriesActor(
      character = "Meredith Palmer",
      totalEpisodes = 202,
      creditId = "525730ac760ee3776a344bfb",
    ),
    profilePath = "/wFXWKB2IUyB6Cu08PyovyBAm9WT.jpg",
  )

  fun creedBratton() = Person(
    id = 85177,
    name = "Creed Bratton",
    role = PersonRole.SeriesActor(
      character = "Creed Bratton",
      totalEpisodes = 194,
      creditId = "525730ac760ee3776a344bc5",
    ),
    profilePath = "/72hNnta4igAn2cE6fDyKElHcZ09.jpg",
  )

  fun leslieDavidBaker() = Person(
    id = 1230842,
    name = "Leslie David Baker",
    role = PersonRole.SeriesActor(
      character = "Stanley Hudson",
      totalEpisodes = 193,
      creditId = "525730ab760ee3776a344a87",
    ),
    profilePath = "/9h3xlV5IYqKinlQCW1ouU7sjwWF.jpg",
  )

  fun phyllisSmith() = Person(
    id = 169200,
    name = "Phyllis Smith",
    role = PersonRole.SeriesActor(
      character = "Phyllis Lapin",
      totalEpisodes = 190,
      creditId = "525730ab760ee3776a344b03",
    ),
    profilePath = "/h9w9pQbiderRWAC2mi7spjzuIGz.jpg",
  )

  fun rainnWilson() = Person(
    id = 11678,
    name = "Rainn Wilson",
    role = PersonRole.SeriesActor(
      character = "Dwight Schrute",
      totalEpisodes = 188,
      creditId = "525730a9760ee3776a34474f",
    ),
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
      Person(
        id = 2166017,
        name = "Steve Rostine",
        profilePath = null,
        role = PersonRole.Crew(
          job = "Set Decoration",
          creditId = "5bdaa3990e0a2603b40089a6",
          totalEpisodes = 4,
          department = "Art",
        ),
      ),
      Person(
        id = 1844757,
        name = "Philip D. Shea",
        profilePath = null,
        role = PersonRole.Crew(
          job = "Property Master",
          creditId = "5bdaa766c3a36807820082a8",
          totalEpisodes = 3,
          department = "Art",
        ),
      ),
      Person(
        id = 33562,
        name = "Donald Lee Harris",
        profilePath = null,
        role = PersonRole.Crew(
          job = "Production Design",
          creditId = "5bdaafa2c3a368078b008d77",
          totalEpisodes = 1,
          department = "Art",
        ),
      ),
      Person(
        id = 2166018,
        name = "Melody Melton",
        profilePath = null,
        role = PersonRole.Crew(
          job = "Property Master",
          creditId = "5bdaa3a29251415407007e49",
          totalEpisodes = 1,
          department = "Art",
        ),
      ),
    ),
  )

  fun unsortedCameraDepartment() = SeriesCrewDepartment(
    department = "Camera",
    crewList = listOf(
      Person(
        id = 1215572,
        name = "Randall Einhorn",
        profilePath = null,
        role = PersonRole.Crew(
          job = "Director of Photography",
          creditId = "5bdaa68f92514153f500859f",
          totalEpisodes = 3,
          department = "Camera",
        ),
      ),
      Person(
        id = 1879373,
        name = "Dale Alexander",
        profilePath = null,
        role = PersonRole.Crew(
          job = "Key Grip",
          creditId = "5bdaa7d90e0a2603c60086d9",
          totalEpisodes = 3,
          department = "Camera",
        ),
      ),
      Person(
        id = 2166021,
        name = "Ron Nichols",
        profilePath = null,
        role = PersonRole.Crew(
          job = "Key Grip",
          creditId = "5bdaa3e40e0a2603b1008d3f",
          totalEpisodes = 1,
          department = "Camera",
        ),
      ),
      Person(
        id = 67864,
        name = "Peter Smokler",
        profilePath = null,
        role = PersonRole.Crew(
          job = "Director of Photography",
          creditId = "5bdaa2d4c3a368078f007f5c",
          totalEpisodes = 1,
          department = "Camera",
        ),
      ),
    ),
  )

  fun camera() = SeriesCrewDepartment(
    department = "Camera",
    crewList = listOf(
      Person(
        id = 1215572,
        name = "Randall Einhorn",
        profilePath = null,
        role = PersonRole.Crew(
          job = "Director of Photography",
          creditId = "5bdaa68f92514153f500859f",
          totalEpisodes = 3,
          department = "Camera",
        ),
      ),
      Person(
        id = 1879373,
        name = "Dale Alexander",
        profilePath = null,
        role = PersonRole.Crew(
          job = "Key Grip",
          creditId = "5bdaa7d90e0a2603c60086d9",
          totalEpisodes = 3,
          department = "Camera",
        ),
      ),
      Person(
        id = 2166021,
        name = "Ron Nichols",
        profilePath = null,
        role = PersonRole.Crew(
          job = "Key Grip",
          creditId = "5bdaa3e40e0a2603b1008d3f",
          totalEpisodes = 1,
          department = "Camera",
        ),
      ),
      Person(
        id = 67864,
        name = "Peter Smokler",
        profilePath = null,
        role = PersonRole.Crew(
          job = "Director of Photography",
          creditId = "5bdaa2d4c3a368078f007f5c",
          totalEpisodes = 1,
          department = "Camera",
        ),
      ),
    ),
  )

  fun sortedCameraDepartment() = SeriesCrewDepartment(
    department = "Camera",
    crewList = listOf(
      Person(
        id = 1879373,
        name = "Dale Alexander",
        profilePath = null,
        role = PersonRole.Crew(
          job = "Key Grip",
          creditId = "5bdaa7d90e0a2603c60086d9",
          totalEpisodes = 3,
          department = "Camera",
        ),
      ),
      Person(
        id = 67864,
        name = "Peter Smokler",
        profilePath = null,
        role = PersonRole.Crew(
          job = "Director of Photography",
          creditId = "5bdaa2d4c3a368078f007f5c",
          totalEpisodes = 1,
          department = "Camera",
        ),
      ),
      Person(
        id = 1215572,
        name = "Randall Einhorn",
        profilePath = null,
        role = PersonRole.Crew(
          job = "Director of Photography",
          creditId = "5bdaa68f92514153f500859f",
          totalEpisodes = 3,
          department = "Camera",
        ),
      ),
      Person(
        id = 2166021,
        name = "Ron Nichols",
        profilePath = null,
        role = PersonRole.Crew(
          job = "Key Grip",
          creditId = "5bdaa3e40e0a2603b1008d3f",
          totalEpisodes = 1,
          department = "Camera",
        ),
      ),
    ),
  )

  fun costumeAndMakeUp() = SeriesCrewDepartment(
    department = "Costume & Make-Up",
    crewList = listOf(
      Person(
        id = 1325655,
        name = "Elinor Bardach",
        profilePath = null,
        role = PersonRole.Crew(
          job = "Costume Supervisor",
          creditId = "5bdaa36e92514153fb008795",
          totalEpisodes = 4,
          department = "Costume & Make-Up",
        ),
      ),
      Person(
        id = 2166015,
        name = "Carey Bennett",
        profilePath = null,
        role = PersonRole.Crew(
          job = "Costume Designer",
          creditId = "5bdaa3650e0a2603bf008174",
          totalEpisodes = 4,
          department = "Costume & Make-Up",
        ),
      ),
      Person(
        id = 1664353,
        name = "Cyndra Dunn",
        profilePath = null,
        role = PersonRole.Crew(
          job = "Key Hair Stylist",
          creditId = "5bdaa74d0e0a2603b4008cbf",
          totalEpisodes = 3,
          department = "Costume & Make-Up",
        ),
      ),
      Person(
        id = 1543004,
        name = "Maria Valdivia",
        profilePath = null,
        role = PersonRole.Crew(
          job = "Key Hair Stylist",
          creditId = "5bdaa38cc3a3680772009017",
          totalEpisodes = 1,
          department = "Costume & Make-Up",
        ),
      ),
    ),
  )
}

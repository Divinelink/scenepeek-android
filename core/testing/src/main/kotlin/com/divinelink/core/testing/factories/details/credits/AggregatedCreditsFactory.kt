package com.divinelink.core.testing.factories.details.credits

import com.divinelink.core.model.credits.AggregateCredits
import com.divinelink.core.model.credits.SeriesCast
import com.divinelink.core.model.credits.SeriesCrew
import com.divinelink.core.model.credits.SeriesCrewDepartment

object AggregatedCreditsFactory {

  fun credits() = AggregateCredits(
    id = 2316,
    cast = SeriesCastFactory.cast(),
    crewDepartments = SeriesCrewListFactory.crewDepartments(),
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

  fun brianBaumgartner() = SeriesCast(
    id = 94622,
    adult = false,
    name = "Brian Baumgartner",
    totalEpisodes = 217,
    character = "Kevin Malone",
    profilePath = "/1O7ECkD4mOKAgMAbQADBpTKBzOP.jpg",
  )

  fun angelaKinsey() = SeriesCast(
    id = 113867,
    adult = false,
    name = "Angela Kinsey",
    totalEpisodes = 210,
    character = "Angela Martin",
    profilePath = "/qHFidnMcFqUWdMFyjmXufBlYckd.jpg",
  )

  fun oscarNunez() = SeriesCast(
    id = 76094,
    adult = false,
    name = "Óscar Núñez",
    totalEpisodes = 203,
    character = "Óscar Martínez",
    profilePath = "/UBILHiRphJdlshvsyH920QSAhk.jpg",
  )

  fun kateFlannery() = SeriesCast(
    id = 304282,
    adult = false,
    name = "Kate Flannery",
    totalEpisodes = 202,
    character = "Meredith Palmer",
    profilePath = "/wFXWKB2IUyB6Cu08PyovyBAm9WT.jpg",
  )

  fun creedBratton() = SeriesCast(
    id = 85177,
    adult = false,
    name = "Creed Bratton",
    totalEpisodes = 194,
    character = "Creed Bratton",
    profilePath = "/72hNnta4igAn2cE6fDyKElHcZ09.jpg",
  )

  fun leslieDavidBaker() = SeriesCast(
    id = 1230842,
    adult = false,
    name = "Leslie David Baker",
    totalEpisodes = 193,
    character = "Stanley Hudson",
    profilePath = "/9h3xlV5IYqKinlQCW1ouU7sjwWF.jpg",
  )

  fun phyllisSmith() = SeriesCast(
    id = 169200,
    adult = false,
    name = "Phyllis Smith",
    totalEpisodes = 190,
    character = "Phyllis Lapin",
    profilePath = "/h9w9pQbiderRWAC2mi7spjzuIGz.jpg",
  )

  fun rainnWilson() = SeriesCast(
    id = 11678,
    adult = false,
    name = "Rainn Wilson",
    totalEpisodes = 188,
    character = "Dwight Schrute",
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
      SeriesCrew(
        id = 2166017,
        creditId = "5bdaa3990e0a2603b40089a6",
        adult = false,
        name = "Steve Rostine",
        episodeCount = 4,
        job = "Set Decoration",
        profilePath = null,
        department = "Art",
      ),
      SeriesCrew(
        id = 1844757,
        creditId = "5bdaa766c3a36807820082a8",
        adult = false,
        name = "Philip D. Shea",
        episodeCount = 3,
        job = "Property Master",
        profilePath = null,
        department = "Art",
      ),
      SeriesCrew(
        id = 33562,
        creditId = "5bdaafa2c3a368078b008d77",
        adult = false,
        name = "Donald Lee Harris",
        episodeCount = 1,
        job = "Production Design",
        profilePath = null,
        department = "Art",
      ),
      SeriesCrew(
        id = 2166018,
        creditId = "5bdaa3a29251415407007e49",
        adult = false,
        name = "Melody Melton",
        episodeCount = 1,
        job = "Property Master",
        profilePath = null,
        department = "Art",
      ),
    ),
  )

  fun camera() = SeriesCrewDepartment(
    department = "Camera",
    crewList = listOf(
      SeriesCrew(
        id = 1215572,
        creditId = "5bdaa68f92514153f500859f",
        adult = false,
        name = "Randall Einhorn",
        episodeCount = 3,
        job = "Director of Photography",
        profilePath = null,
        department = "Camera",
      ),
      SeriesCrew(
        id = 1879373,
        creditId = "5bdaa7d90e0a2603c60086d9",
        adult = false,
        name = "Dale Alexander",
        episodeCount = 3,
        job = "Key Grip",
        profilePath = null,
        department = "Camera",
      ),
      SeriesCrew(
        id = 2166021,
        creditId = "5bdaa3e40e0a2603b1008d3f",
        adult = false,
        name = "Ron Nichols",
        episodeCount = 1,
        job = "Key Grip",
        profilePath = null,
        department = "Camera",
      ),
      SeriesCrew(
        id = 67864,
        creditId = "5bdaa2d4c3a368078f007f5c",
        adult = false,
        name = "Peter Smokler",
        episodeCount = 1,
        job = "Director of Photography",
        profilePath = null,
        department = "Camera",
      ),
    ),
  )

  fun costumeAndMakeUp() = SeriesCrewDepartment(
    department = "Costume & Make-Up",
    crewList = listOf(
      SeriesCrew(
        id = 1325655,
        creditId = "5bdaa36e92514153fb008795",
        adult = false,
        name = "Elinor Bardach",
        episodeCount = 4,
        job = "Costume Supervisor",
        profilePath = null,
        department = "Costume & Make-Up",
      ),
      SeriesCrew(
        id = 2166015,
        creditId = "5bdaa3650e0a2603bf008174",
        adult = false,
        name = "Carey Bennett",
        episodeCount = 4,
        job = "Costume Designer",
        profilePath = null,
        department = "Costume & Make-Up",
      ),
      SeriesCrew(
        id = 1664353,
        creditId = "5bdaa74d0e0a2603b4008cbf",
        adult = false,
        name = "Cyndra Dunn",
        episodeCount = 3,
        job = "Key Hair Stylist",
        profilePath = null,
        department = "Costume & Make-Up",
      ),
      SeriesCrew(
        id = 1543004,
        creditId = "5bdaa38cc3a3680772009017",
        adult = false,
        name = "Maria Valdivia",
        episodeCount = 1,
        job = "Key Hair Stylist",
        profilePath = null,
        department = "Costume & Make-Up",
      ),
    ),
  )
}

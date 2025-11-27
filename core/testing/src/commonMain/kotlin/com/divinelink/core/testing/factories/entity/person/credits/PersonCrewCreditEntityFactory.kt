package com.divinelink.core.testing.factories.entity.person.credits

import com.divinelink.core.database.person.credits.PersonCrewCreditEntity

object PersonCrewCreditEntityFactory {

  fun the40YearOldVirgin(): PersonCrewCreditEntity = PersonCrewCreditEntity(
    id = 6957,
    releaseDate = "2005-08-11",
    personId = 4495,
    firstAirDate = null,
    creditId = "52fe446ac3a36847f8094c49",
    department = "Writing",
    job = "Screenplay",
    episodeCount = null,
    mediaType = "movie",
  )

  fun getSmart(): PersonCrewCreditEntity = PersonCrewCreditEntity(
    id = 11665,
    releaseDate = "2008-06-19",
    personId = 4495,
    firstAirDate = null,
    creditId = "52fe44749251416c750355d3",
    department = "Production",
    job = "Executive Producer",
    episodeCount = null,
    mediaType = "movie",
  )

  fun theIncredibleBurtWonderstone(): PersonCrewCreditEntity = PersonCrewCreditEntity(
    id = 124459,
    releaseDate = "2013-03-14",
    personId = 4495,
    firstAirDate = null,
    creditId = "5640b22e925141705c00145f",
    department = "Production",
    job = "Producer",
    episodeCount = null,
    mediaType = "movie",
  )

  fun riot() = PersonCrewCreditEntity(
    id = 60734,
    firstAirDate = "2014-05-13",
    creditId = "53762236c3a3681ed4001579",
    department = "Production",
    job = "Executive Producer",
    episodeCount = 4,
    mediaType = "tv",
    personId = 4495,
    releaseDate = null,
  )

  fun all() = listOf(
    the40YearOldVirgin(),
    getSmart(),
    theIncredibleBurtWonderstone(),
    riot(),
  )

  fun sortedByDate() = listOf(
    riot(),
    theIncredibleBurtWonderstone(),
    getSmart(),
    the40YearOldVirgin(),
  )
}

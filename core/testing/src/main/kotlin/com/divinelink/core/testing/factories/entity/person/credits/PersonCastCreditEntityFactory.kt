package com.divinelink.core.testing.factories.entity.person.credits

import com.divinelink.core.database.person.credits.PersonCastCreditEntity

object PersonCastCreditEntityFactory {

  fun bruceAlmighty(): PersonCastCreditEntity = PersonCastCreditEntity(
    id = 310,
    releaseDate = "2003-05-23",
    character = "Evan Baxter",
    creditId = "52fe4236c3a36847f800c65f",
    mediaType = "movie",
    personId = 4495,
    firstAirDate = null,
    episodeCount = null,
    creditOrder = 6,
  )

  fun littleMissSunshine(): PersonCastCreditEntity = PersonCastCreditEntity(
    id = 773,
    releaseDate = "2006-07-26",
    character = "Frank Ginsberg",
    creditId = "52fe4274c3a36847f80200d3",
    mediaType = "movie",
    personId = 4495,
    firstAirDate = null,
    episodeCount = null,
    creditOrder = 2,
  )

  fun despicableMe(): PersonCastCreditEntity = PersonCastCreditEntity(
    id = 20352,
    releaseDate = "2010-07-08",
    character = "Gru (voice)",
    creditId = "52fe43e2c3a368484e003e77",
    mediaType = "movie",
    personId = 4495,
    firstAirDate = null,
    episodeCount = null,
    creditOrder = 0,
  )

  fun theOffice(): PersonCastCreditEntity = PersonCastCreditEntity(
    id = 2316,
    releaseDate = null,
    character = "Michael Scott",
    creditId = "525730a9760ee3776a344705",
    mediaType = "tv",
    personId = 4495,
    firstAirDate = "2005-03-24",
    episodeCount = 140,
    creditOrder = null,
  )

  fun all() = listOf(
    bruceAlmighty(),
    littleMissSunshine(),
    despicableMe(),
    theOffice(),
  )

  fun sortedByDate() = listOf(
    despicableMe(),
    littleMissSunshine(),
    theOffice(),
    bruceAlmighty(),
  )

  fun sortedByPopularity() = listOf(
    theOffice(),
    despicableMe(),
    bruceAlmighty(),
    littleMissSunshine(),
  )
}

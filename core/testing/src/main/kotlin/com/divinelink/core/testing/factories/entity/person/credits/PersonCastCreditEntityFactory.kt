package com.divinelink.core.testing.factories.entity.person.credits

import com.divinelink.core.database.person.credits.PersonCastCreditEntity

object PersonCastCreditEntityFactory {

  fun bruceAlmighty(): PersonCastCreditEntity = PersonCastCreditEntity(
    adult = 0,
    backdropPath = "/8bQbejOvAKe78RXxpkfM04n0K18.jpg",
    genreIds = "14, 35",
    id = 310,
    originalLanguage = "en",
    originalTitle = "Bruce Almighty",
    overview = "Bruce Nolan toils as a \"human interest\"" +
      " television reporter in Buffalo, NY, " +
      "but despite his high ratings and the love of his" +
      " beautiful girlfriend, Bruce remains unfulfilled." +
      " At the end of the worst day in his life, he angrily " +
      "ridicules God - and the Almighty responds, " +
      "endowing Bruce with all of His divine powers.",
    popularity = 58.75,
    posterPath = "/3XJKBKh9Km89EoUEitVTSnrlAkZ.jpg",
    releaseDate = "2003-05-23",
    title = "Bruce Almighty",
    video = 0,
    voteAverage = 6.713,
    voteCount = 10485,
    character = "Evan Baxter",
    creditId = "52fe4236c3a36847f800c65f",
    mediaType = "movie",
    personId = 4495,
    originalName = null,
    firstAirDate = null,
    name = null,
    episodeCount = null,
    originCountry = null,
  )

  fun littleMissSunshine(): PersonCastCreditEntity = PersonCastCreditEntity(
    adult = 0,
    backdropPath = "/4YzGG8IPduUQRVnQ0vN5laHA1rH.jpg",
    genreIds = "35, 18",
    id = 773,
    originalLanguage = "en",
    originalTitle = "Little Miss Sunshine",
    overview = "A family loaded with quirky, colorful characters " +
      "piles into an old van and road trips to California for " +
      "little Olive to compete in a beauty pageant.",
    popularity = 21.03,
    posterPath = "/wKn7AJw730emlmzLSmJtzquwaeW.jpg",
    releaseDate = "2006-07-26",
    title = "Little Miss Sunshine",
    video = 0,
    voteAverage = 7.689,
    voteCount = 6948,
    character = "Frank Ginsberg",
    creditId = "52fe4274c3a36847f80200d3",
    mediaType = "movie",
    personId = 4495,
    originalName = null,
    firstAirDate = null,
    name = null,
    episodeCount = null,
    originCountry = null,
  )

  fun despicableMe(): PersonCastCreditEntity = PersonCastCreditEntity(
    adult = 0,
    backdropPath = "/2XSeKDmIa2KxaiJy4J9e8FrIZhk.jpg",
    genreIds = "10751, 16, 35",
    id = 20352,
    originalLanguage = "en",
    originalTitle = "Despicable Me",
    overview = "Villainous Gru lives up to his reputation as a despicable," +
      " deplorable and downright unlikable guy when he hatches a plan " +
      "to steal the moon from the sky. But he has a tough time" +
      " staying on task after three orphans land in his care.",
    popularity = 240.938,
    posterPath = "/9lOloREsAhBu0pEtU0BgeR1rHyo.jpg",
    releaseDate = "2010-07-08",
    title = "Despicable Me",
    video = 0,
    voteAverage = 7.244,
    voteCount = 14804,
    character = "Gru (voice)",
    creditId = "52fe43e2c3a368484e003e77",
    mediaType = "movie",
    personId = 4495,
    originalName = null,
    firstAirDate = null,
    name = null,
    episodeCount = null,
    originCountry = null,
  )

  fun theOffice(): PersonCastCreditEntity = PersonCastCreditEntity(
    adult = 0,
    backdropPath = "/mLyW3UTgi2lsMdtueYODcfAB9Ku.jpg",
    genreIds = "35",
    id = 2316,
    originalLanguage = "en",
    originalTitle = null,
    originalName = "The Office",
    overview = "The everyday lives of office employees in the Scranton," +
      " Pennsylvania branch of the fictional Dunder Mifflin Paper Company.",
    popularity = 400.887,
    posterPath = "/7DJKHzAi83BmQrWLrYYOqcoKfhR.jpg",
    releaseDate = null,
    title = null,
    video = 0,
    voteAverage = 8.578,
    voteCount = 4020,
    character = "Michael Scott",
    creditId = "525730a9760ee3776a344705",
    mediaType = "tv",
    personId = 4495,
    firstAirDate = "2005-03-24",
    name = "The Office",
    episodeCount = 140,
    originCountry = "US",
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

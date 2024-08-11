package com.divinelink.core.testing.factories.api.details.person

import com.divinelink.core.network.details.person.model.PersonCastCreditApi

/**
 * First 3 entries from "person-combined-credits.json"
 */
object PersonCastCreditApiFactory {

  fun bruceAlmighty() = PersonCastCreditApi(
    adult = false,
    backdropPath = "/8bQbejOvAKe78RXxpkfM04n0K18.jpg",
    genreIds = listOf(14, 35),
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
    video = false,
    voteAverage = 6.713,
    voteCount = 10485,
    character = "Evan Baxter",
    creditId = "52fe4236c3a36847f800c65f",
    order = 6,
    mediaType = "movie",
  )

  fun littleMissSunshine() = PersonCastCreditApi(
    adult = false,
    backdropPath = "/4YzGG8IPduUQRVnQ0vN5laHA1rH.jpg",
    genreIds = listOf(35, 18),
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
    video = false,
    voteAverage = 7.689,
    voteCount = 6948,
    character = "Frank Ginsberg",
    creditId = "52fe4274c3a36847f80200d3",
    order = 2,
    mediaType = "movie",
  )

  fun despicableMe() = PersonCastCreditApi(
    adult = false,
    backdropPath = "/2XSeKDmIa2KxaiJy4J9e8FrIZhk.jpg",
    genreIds = listOf(10751, 16, 35),
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
    video = false,
    voteAverage = 7.244,
    voteCount = 14804,
    character = "Gru (voice)",
    creditId = "52fe43e2c3a368484e003e77",
    order = 0,
    mediaType = "movie",
  )

  fun theOffice() = PersonCastCreditApi(
    adult = false,
    backdropPath = "/mLyW3UTgi2lsMdtueYODcfAB9Ku.jpg",
    genreIds = listOf(35),
    id = 2316,
    originCountry = listOf("US"),
    originalLanguage = "en",
    originalName = "The Office",
    overview = "The everyday lives of office employees in the Scranton," +
      " Pennsylvania branch of the fictional Dunder Mifflin Paper Company.",
    popularity = 400.887,
    posterPath = "/7DJKHzAi83BmQrWLrYYOqcoKfhR.jpg",
    firstAirDate = "2005-03-24",
    name = "The Office",
    voteAverage = 8.578,
    voteCount = 4020,
    character = "Michael Scott",
    creditId = "525730a9760ee3776a344705",
    episodeCount = 140,
    mediaType = "tv",
  )

  fun all() = listOf(
    bruceAlmighty(),
    littleMissSunshine(),
    despicableMe(),
    theOffice(),
  )
}

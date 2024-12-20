package com.divinelink.core.fixtures.model.person.credit

import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.person.credits.PersonMovieCastCredit
import com.divinelink.core.model.person.credits.PersonTVCastCredit

object PersonCastCreditFactory {

  fun bruceAlmighty() = PersonMovieCastCredit(
    id = 310,
    title = "Bruce Almighty",
    originalTitle = "Bruce Almighty",
    posterPath = "/3XJKBKh9Km89EoUEitVTSnrlAkZ.jpg",
    mediaType = MediaType.MOVIE,
    adult = false,
    backdropPath = "/8bQbejOvAKe78RXxpkfM04n0K18.jpg",
    genreIds = listOf(14, 35),
    originalLanguage = "en",
    overview = "Bruce Nolan toils as a \"human interest\"" +
      " television reporter in Buffalo, NY, " +
      "but despite his high ratings and the love of his" +
      " beautiful girlfriend, Bruce remains unfulfilled." +
      " At the end of the worst day in his life, he angrily " +
      "ridicules God - and the Almighty responds, " +
      "endowing Bruce with all of His divine powers.",
    popularity = 58.75,
    releaseDate = "2003-05-23",
    voteAverage = 6.713,
    voteCount = 10485,
    creditId = "52fe4236c3a36847f800c65f",
    video = false,
    order = 6,
    character = "Evan Baxter",
  )

  fun littleMissSunshine() = PersonMovieCastCredit(
    id = 773,
    title = "Little Miss Sunshine",
    originalTitle = "Little Miss Sunshine",
    posterPath = "/wKn7AJw730emlmzLSmJtzquwaeW.jpg",
    mediaType = MediaType.MOVIE,
    adult = false,
    backdropPath = "/4YzGG8IPduUQRVnQ0vN5laHA1rH.jpg",
    genreIds = listOf(35, 18),
    originalLanguage = "en",
    overview = "A family loaded with quirky, colorful characters " +
      "piles into an old van and road trips to California for " +
      "little Olive to compete in a beauty pageant.",
    popularity = 21.03,
    releaseDate = "2006-07-26",
    voteAverage = 7.689,
    voteCount = 6948,
    creditId = "52fe4274c3a36847f80200d3",
    video = false,
    order = 2,
    character = "Frank Ginsberg",
  )

  fun despicableMe() = PersonMovieCastCredit(
    id = 20352,
    title = "Despicable Me",
    originalTitle = "Despicable Me",
    posterPath = "/9lOloREsAhBu0pEtU0BgeR1rHyo.jpg",
    mediaType = MediaType.MOVIE,
    adult = false,
    backdropPath = "/2XSeKDmIa2KxaiJy4J9e8FrIZhk.jpg",
    genreIds = listOf(10751, 16, 35),
    originalLanguage = "en",
    overview = "Villainous Gru lives up to his reputation as a despicable," +
      " deplorable and downright unlikable guy when he hatches a plan " +
      "to steal the moon from the sky. But he has a tough time" +
      " staying on task after three orphans land in his care.",
    popularity = 240.938,
    releaseDate = "2010-07-08",
    voteAverage = 7.244,
    voteCount = 14804,
    creditId = "52fe43e2c3a368484e003e77",
    video = false,
    order = 0,
    character = "Gru (voice)",
  )

  fun theOffice() = PersonTVCastCredit(
    id = 2316,
    name = "The Office",
    originalName = "The Office",
    posterPath = "/7DJKHzAi83BmQrWLrYYOqcoKfhR.jpg",
    mediaType = MediaType.TV,
    adult = false,
    backdropPath = "/mLyW3UTgi2lsMdtueYODcfAB9Ku.jpg",
    genreIds = listOf(35),
    originalLanguage = "en",
    overview = "The everyday lives of office employees in the Scranton," +
      " Pennsylvania branch of the fictional Dunder Mifflin Paper Company.",
    popularity = 400.887,
    firstAirDate = "2005-03-24",
    voteAverage = 8.578,
    voteCount = 4020,
    creditId = "525730a9760ee3776a344705",
    episodeCount = 140,
    character = "Michael Scott",
    originCountry = listOf("US"),
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

  fun knownFor() = listOf(
    theOffice(),
    littleMissSunshine(),
    despicableMe(),
    bruceAlmighty(),
  )
}

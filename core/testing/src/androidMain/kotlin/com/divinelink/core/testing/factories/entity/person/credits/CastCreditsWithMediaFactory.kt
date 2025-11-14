package com.divinelink.core.testing.factories.entity.person.credits

import com.divinelink.core.database.person.credits.CastCreditsWithMedia
import com.divinelink.core.model.media.MediaType

object CastCreditsWithMediaFactory {

  val despicableMe = CastCreditsWithMedia(
    backdropPath = "/2XSeKDmIa2KxaiJy4J9e8FrIZhk.jpg",
    id = 20352,
    overview = "Villainous Gru lives up to his reputation as a despicable," +
      " deplorable and downright unlikable guy when he hatches a plan " +
      "to steal the moon from the sky. But he has a tough time" +
      " staying on task after three orphans land in his care.",
    popularity = 240.938,
    posterPath = "/9lOloREsAhBu0pEtU0BgeR1rHyo.jpg",
    releaseDate = "2010-07-08",
    voteAverage = 7.244,
    voteCount = 14804,
    creditId = "52fe43e2c3a368484e003e77",
    personId = 4495,
    creditOrder = 0,
    character = "Gru (voice)",
    mediaType = MediaType.MOVIE.value,
    adult = -1,
    originalLanguage = "",
    video = -1,
    episodeCount = null,
    firstAirDate = null,
    name = "Despicable Me",
    originalName = "Despicable Me",
    genreIdsJson = "",
    originCountryJson = "",
  )

  val bruceAlmighty = CastCreditsWithMedia(
    id = 310,
    personId = 4495,
    posterPath = "/3XJKBKh9Km89EoUEitVTSnrlAkZ.jpg",
    mediaType = MediaType.MOVIE.value,
    adult = -1,
    backdropPath = "/8bQbejOvAKe78RXxpkfM04n0K18.jpg",
    originalLanguage = "",
    name = "Bruce Almighty",
    originalName = "Bruce Almighty",
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
    genreIdsJson = "",
    video = -1,
    creditOrder = 6,
    character = "Evan Baxter",
    firstAirDate = null,
    episodeCount = null,
    originCountryJson = "",
  )

  val theOffice = CastCreditsWithMedia(
    id = 2316,
    name = "The Office",
    originalName = "The Office",
    posterPath = "/7DJKHzAi83BmQrWLrYYOqcoKfhR.jpg",
    mediaType = MediaType.TV.value,
    adult = -1,
    backdropPath = "/mLyW3UTgi2lsMdtueYODcfAB9Ku.jpg",
    originalLanguage = "",
    overview = "The everyday lives of office employees in the Scranton," +
      " Pennsylvania branch of the fictional Dunder Mifflin Paper Company.",
    popularity = 530.095,
    firstAirDate = "2005-03-24",
    voteAverage = 8.6,
    voteCount = 4503,
    creditId = "525730a9760ee3776a344705",
    episodeCount = 140,
    character = "Michael Scott",
    genreIdsJson = "",
    personId = 4495,
    releaseDate = null,
    creditOrder = null,
    video = -1,
    originCountryJson = "",
  )

  val littleMissSunshine = CastCreditsWithMedia(
    backdropPath = "/4YzGG8IPduUQRVnQ0vN5laHA1rH.jpg",
    id = 773,
    overview = "A family loaded with quirky, colorful characters " +
      "piles into an old van and road trips to California for " +
      "little Olive to compete in a beauty pageant.",
    popularity = 21.03,
    posterPath = "/wKn7AJw730emlmzLSmJtzquwaeW.jpg",
    releaseDate = "2006-07-26",
    name = "Little Miss Sunshine",
    originalName = "Little Miss Sunshine",
    originalLanguage = "",
    voteAverage = 7.689,
    voteCount = 6948,
    creditId = "52fe4274c3a36847f80200d3",
    personId = 4495,
    firstAirDate = null,
    creditOrder = 2,
    character = "Frank Ginsberg",
    episodeCount = null,
    mediaType = MediaType.MOVIE.value,
    adult = -1,
    video = -1,
    originCountryJson = "",
    genreIdsJson = "",
  )

  val all = listOf(
    bruceAlmighty,
    littleMissSunshine,
    despicableMe,
    theOffice,
  )

  val sortedByDate = listOf(
    despicableMe,
    littleMissSunshine,
    theOffice,
    bruceAlmighty,
  )
}

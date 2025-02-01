package com.divinelink.core.testing.factories.api.movie

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.divinelink.core.network.media.model.movie.MovieApi

object MovieApiFactory {

  fun Empty() = MovieApi(
    adult = false,
    backdropPath = null,
    genreIds = listOf(),
    id = 0,
    originalLanguage = "",
    originalTitle = "",
    overview = "",
    popularity = 0.0,
    posterPath = null,
    releaseDate = "",
    title = "",
    video = false,
    voteAverage = 0.0,
    voteCount = null,
  )

  fun Full() = MovieApi(
    id = 1,
    adult = false,
    backdropPath = "",
    genreIds = listOf(),
    originalLanguage = "",
    originalTitle = "",
    overview = "",
    popularity = 0.0,
    posterPath = "",
    releaseDate = "",
    title = "",
    video = false,
    voteAverage = 0.0,
    voteCount = 0,
  )

  fun EmptyList(range: IntProgression = 1..10): List<MovieApi> = (range).map {
    MovieApi(
      id = it,
      adult = false,
      backdropPath = null,
      genreIds = listOf(),
      originalLanguage = "",
      originalTitle = "",
      overview = LoremIpsum(15).values.joinToString(),
      popularity = 0.0,
      posterPath = "movie $it - posterPath",
      releaseDate = "2002-08-22",
      title = "Fight club $it",
      video = false,
      voteAverage = (it + 0.513) % 10,
      voteCount = 12_345 + it,
    )
  }
}

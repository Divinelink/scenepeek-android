package com.divinelink.core.testing.factories.api

import com.divinelink.core.network.media.model.movie.MovieApi

object PopularMovieApiFactory {

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
    voteCount = null
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
    voteCount = 0
  )

  fun EmptyList(
    range: IntProgression = 1..10,
  ): List<MovieApi> = (range).map {
    MovieApi(
      id = it,
      adult = false,
      backdropPath = null,
      genreIds = listOf(),
      originalLanguage = "",
      originalTitle = "",
      overview = "overview $it",
      popularity = 0.0,
      posterPath = "movie $it - posterPath",
      releaseDate = "movie $it - releaseDate",
      title = "movie $it - name",
      video = false,
      voteAverage = it + 0.713,
      voteCount = null
    )
  }
}

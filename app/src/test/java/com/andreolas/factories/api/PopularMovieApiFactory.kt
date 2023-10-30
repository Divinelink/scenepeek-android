package com.andreolas.factories.api

import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularMovieApi

object PopularMovieApiFactory {

  fun Empty() = PopularMovieApi(
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

  fun Full() = PopularMovieApi(
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
  ): List<PopularMovieApi> = (range).map {
    PopularMovieApi(
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
      voteAverage = it.toDouble(),
      voteCount = null
    )
  }
}

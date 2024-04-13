package com.andreolas.factories.api

import com.andreolas.movierama.base.data.remote.movies.dto.search.movie.SearchMovieApi

object SearchMovieApiFactory {

  fun Empty() = SearchMovieApi(
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

  fun EmptyList(
    range: IntProgression = 1..10,
  ): List<SearchMovieApi> = (range).map {
    SearchMovieApi(
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

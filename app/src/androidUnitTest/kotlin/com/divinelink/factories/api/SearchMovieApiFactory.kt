package com.divinelink.factories.api

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.divinelink.core.network.media.model.search.movie.SearchMovieApi

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
    voteCount = null,
  )

  fun EmptyList(range: IntProgression = 1..10): List<SearchMovieApi> = (range).map {
    SearchMovieApi(
      id = it,
      adult = false,
      backdropPath = "movie $it - backdropPath",
      genreIds = listOf(),
      originalLanguage = "",
      originalTitle = "",
      overview = LoremIpsum(15).values.joinToString(),
      popularity = (it * 525.25),
      posterPath = "movie $it - posterPath",
      releaseDate = "2002-08-22",
      title = "Fight club $it",
      video = false,
      voteAverage = (it + 0.513) % 10,
      voteCount = 12_345 + it,
    )
  }
}

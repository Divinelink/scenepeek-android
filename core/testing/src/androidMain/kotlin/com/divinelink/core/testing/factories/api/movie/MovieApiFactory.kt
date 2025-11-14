package com.divinelink.core.testing.factories.api.movie

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.divinelink.core.network.media.model.movie.MovieResponseApi

object MovieApiFactory {

  fun EmptyList(range: IntProgression = 1..10): List<MovieResponseApi> = (range).map {
    MovieResponseApi(
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

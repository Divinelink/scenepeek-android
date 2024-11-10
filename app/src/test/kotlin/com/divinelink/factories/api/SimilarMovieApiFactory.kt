package com.divinelink.factories.api

import com.divinelink.core.network.media.model.details.similar.SimilarMovieApi

object SimilarMovieApiFactory {

  fun Empty() = SimilarMovieApi(
    id = 0,
    adult = false,
    backdropPath = null,
    genreIds = listOf(),
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

  fun Full() = SimilarMovieApi(
    id = 1,
    adult = false,
    backdropPath = "backdrop.jpg",
    genreIds = listOf(),
    originalLanguage = "Original Language",
    originalTitle = "Original Title",
    overview = "Overview",
    popularity = 750.0,
    posterPath = "similar_movie_poster.jpg",
    releaseDate = "2000",
    title = "Similar movie title",
    video = false,
    voteAverage = 9.85444334,
    voteCount = null,
  )

  fun SimilarMovieApiList(range: IntProgression = 1..10) = range.map {
    SimilarMovieApi(
      id = it,
      adult = false,
      backdropPath = if (it % 2 == 0) "backdrop.jpg" else null,
      genreIds = listOf(it),
      originalLanguage = "Lorem Ipsum language $it",
      originalTitle = "Lorem Ipsum title $it",
      overview = "Lorem Ipsum $it",
      popularity = it.toDouble(),
      posterPath = if (it % 2 == 0) ".jpg" else null,
      releaseDate = (2000 + it).toString(),
      title = "Lorem Ipsum title",
      video = false,
      voteAverage = 9.85444334,
      voteCount = null,
    )
  }
}

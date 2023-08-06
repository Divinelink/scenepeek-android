package com.andreolas.movierama.base.data.remote.movies.dto.search.multi.mapper

import com.andreolas.movierama.base.data.remote.movies.dto.search.multi.MultiSearchResponseApi
import com.andreolas.movierama.home.domain.model.MediaType
import com.andreolas.movierama.home.domain.model.Search
import gr.divinelink.core.util.extensions.round

fun MultiSearchResponseApi.map(): List<Search> = results.map {
  when (MediaType.from(it.mediaType)) {

    MediaType.TV -> Search.Media.TV(
      id = it.id,
      posterPath = it.posterPath,
      releaseDate = it.releaseDate ?: "",
      name = it.name!!,
      rating = it.voteAverage?.round(1).toString(),
      overview = it.overview ?: "",
      isFavorite = false,
    )
    MediaType.MOVIE -> Search.Media.Movie(
      id = it.id,
      posterPath = it.posterPath,
      releaseDate = it.releaseDate ?: "",
      name = it.title!!,
      rating = it.voteAverage?.round(1).toString(),
      overview = it.overview!!,
      isFavorite = false,
    )
    MediaType.PERSON -> Search.Person(
      id = it.id,
      posterPath = it.posterPath ?: "",
      name = it.name ?: "",
    )
    MediaType.UNKNOWN -> Search.Unknown
  }
}

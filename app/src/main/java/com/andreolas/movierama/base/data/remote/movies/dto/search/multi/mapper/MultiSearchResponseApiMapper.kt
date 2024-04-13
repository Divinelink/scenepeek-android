package com.andreolas.movierama.base.data.remote.movies.dto.search.multi.mapper

import com.andreolas.movierama.base.data.remote.movies.dto.search.multi.MultiSearchResponseApi
import com.andreolas.movierama.home.domain.model.MediaItem
import com.andreolas.movierama.home.domain.model.MediaType
import gr.divinelink.core.util.extensions.round

fun MultiSearchResponseApi.map(): List<MediaItem> = results.map {
  when (MediaType.from(it.mediaType)) {

    MediaType.TV -> MediaItem.Media.TV(
      id = it.id,
      posterPath = it.posterPath,
      releaseDate = it.releaseDate ?: "",
      name = it.name!!,
      rating = it.voteAverage?.round(1).toString(),
      overview = it.overview ?: "",
      isFavorite = false,
    )
    MediaType.MOVIE -> MediaItem.Media.Movie(
      id = it.id,
      posterPath = it.posterPath,
      releaseDate = it.releaseDate ?: "",
      name = it.title!!,
      rating = it.voteAverage?.round(1).toString(),
      overview = it.overview!!,
      isFavorite = false,
    )
    MediaType.PERSON -> MediaItem.Person(
      id = it.id,
      posterPath = it.posterPath ?: "",
      name = it.name ?: "",
    )
    MediaType.UNKNOWN -> MediaItem.Unknown
  }
}

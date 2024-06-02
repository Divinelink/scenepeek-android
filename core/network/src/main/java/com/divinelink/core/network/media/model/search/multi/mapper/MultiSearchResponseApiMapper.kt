package com.divinelink.core.network.media.model.search.multi.mapper

import com.divinelink.core.commons.extensions.round
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.model.search.multi.MultiSearchResponseApi

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

package com.divinelink.core.network.media.model.search.multi.mapper

import com.divinelink.core.commons.extensions.round
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.model.search.multi.MultiSearchResultApi

fun List<MultiSearchResultApi>.mapToMedia(): List<MediaItem.Media> = this.mapNotNull {
  when (MediaType.from(it.mediaType)) {
    MediaType.TV -> MediaItem.Media.TV(
      id = it.id,
      posterPath = it.posterPath,
      releaseDate = it.firstAirDate ?: "",
      name = it.name!!,
      voteAverage = it.voteAverage?.round(1) ?: 0.0,
      voteCount = it.voteCount ?: 0,
      overview = it.overview ?: "",
      isFavorite = false,
    )
    MediaType.MOVIE -> MediaItem.Media.Movie(
      id = it.id,
      posterPath = it.posterPath,
      releaseDate = it.releaseDate ?: "",
      name = it.title!!,
      voteAverage = it.voteAverage?.round(1) ?: 0.0,
      voteCount = it.voteCount ?: 0,
      overview = it.overview ?: "",
      isFavorite = false,
    )
    MediaType.PERSON -> null
    MediaType.UNKNOWN -> null
  }
}

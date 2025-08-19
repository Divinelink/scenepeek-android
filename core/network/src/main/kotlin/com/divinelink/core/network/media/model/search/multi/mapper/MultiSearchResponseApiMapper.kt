package com.divinelink.core.network.media.model.search.multi.mapper

import com.divinelink.core.commons.extensions.round
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.person.Gender
import com.divinelink.core.model.search.MultiSearch
import com.divinelink.core.network.media.model.search.multi.MultiSearchResponseApi
import com.divinelink.core.network.media.model.search.multi.MultiSearchResultApi

fun MultiSearchResponseApi.map(): MultiSearch = MultiSearch(
  searchList = results.map(),
  totalPages = totalPages,
)

fun List<MultiSearchResultApi>.map(): List<MediaItem> = this.map {
  when (MediaType.from(it.mediaType)) {
    MediaType.TV -> MediaItem.Media.TV(
      id = it.id,
      posterPath = it.posterPath,
      backdropPath = it.backdropPath,
      releaseDate = it.firstAirDate ?: "",
      name = it.name!!,
      voteAverage = it.voteAverage?.round(1) ?: 0.0,
      voteCount = it.voteCount ?: 0,
      overview = it.overview ?: "",
      popularity = it.popularity,
      isFavorite = false,
    )
    MediaType.MOVIE -> MediaItem.Media.Movie(
      id = it.id,
      posterPath = it.posterPath,
      backdropPath = it.backdropPath,
      releaseDate = it.releaseDate ?: "",
      name = it.title!!,
      voteAverage = it.voteAverage?.round(1) ?: 0.0,
      voteCount = it.voteCount ?: 0,
      overview = it.overview!!,
      popularity = it.popularity,
      isFavorite = false,
    )
    MediaType.PERSON -> MediaItem.Person(
      id = it.id,
      profilePath = it.profilePath ?: "",
      name = it.name ?: "",
      knownForDepartment = it.knownForDepartment,
      gender = Gender.from(it.gender),
    )
    MediaType.UNKNOWN -> MediaItem.Unknown
  }
}

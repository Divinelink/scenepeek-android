package com.divinelink.core.network.media.model.search.multi.mapper

import com.divinelink.core.commons.extensions.round
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.person.Gender
import com.divinelink.core.model.search.MultiSearch
import com.divinelink.core.network.media.model.search.multi.MultiSearchResponseApi

fun MultiSearchResponseApi.map(): MultiSearch = MultiSearch(
  searchList = results.map {
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
        overview = it.overview!!,
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
  },
  totalPages = totalPages,
)

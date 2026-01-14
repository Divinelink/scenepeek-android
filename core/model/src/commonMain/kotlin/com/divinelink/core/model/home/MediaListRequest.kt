package com.divinelink.core.model.home

import com.divinelink.core.model.media.MediaType

sealed interface MediaListRequest {
  data object TrendingAll : MediaListRequest
  data class Popular(val mediaType: MediaType) : MediaListRequest
  data class Upcoming(val mediaType: MediaType, val minDate: String) : MediaListRequest
  data class TopRated(val mediaType: MediaType) : MediaListRequest
}

fun HomeSection.toRequest(
  mediaType: MediaType,
): MediaListRequest? {
  return when (this) {
    HomeSection.Favorites -> null
    HomeSection.TrendingAll -> MediaListRequest.TrendingAll
    is HomeSection.Popular -> MediaListRequest.Popular(this.mediaType)
    is HomeSection.Upcoming -> MediaListRequest.Upcoming(this.mediaType, minDate)
    is HomeSection.TopRated -> MediaListRequest.TopRated(mediaType)
  }
}

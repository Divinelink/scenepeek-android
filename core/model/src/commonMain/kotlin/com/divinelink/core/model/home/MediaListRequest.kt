package com.divinelink.core.model.home

import com.divinelink.core.model.media.MediaType

sealed interface MediaListRequest {
  data object TrendingAll : MediaListRequest
  data class Popular(val mediaType: MediaType) : MediaListRequest
  data class Upcoming(val mediaType: MediaType, val minDate: String) : MediaListRequest
  data class TopRated(val mediaType: MediaType) : MediaListRequest
}

fun MediaListSection.toRequest(
  mediaType: MediaType,
): MediaListRequest? {
  return when (this) {
    MediaListSection.Favorites -> null
    MediaListSection.TopRated -> MediaListRequest.TopRated(mediaType)

    MediaListSection.TrendingAll -> MediaListRequest.TrendingAll
    is MediaListSection.Popular -> MediaListRequest.Popular(this.mediaType)
    is MediaListSection.Upcoming -> MediaListRequest.Upcoming(this.mediaType, minDate)
  }
}

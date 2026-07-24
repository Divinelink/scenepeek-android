package com.divinelink.core.network.trakt.service

import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.trakt.model.TraktRatingApi

interface TraktService {
  suspend fun fetchRating(
    mediaType: MediaType,
    imdbId: String,
  ): Result<TraktRatingApi>
}

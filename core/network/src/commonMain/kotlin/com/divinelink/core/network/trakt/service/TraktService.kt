package com.divinelink.core.network.trakt.service

import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.trakt.model.TraktRatingApi
import kotlinx.coroutines.flow.Flow

interface TraktService {
  fun fetchRating(
    mediaType: MediaType,
    imdbId: String,
  ): Flow<TraktRatingApi>
}

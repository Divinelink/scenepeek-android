package com.divinelink.core.network.trakt.service

import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.client.TraktClient
import com.divinelink.core.network.client.get
import com.divinelink.core.network.trakt.model.TraktRatingApi
import com.divinelink.core.network.trakt.util.buildTraktRatingUrl

class ProdTraktService(private val client: TraktClient) : TraktService {

  override suspend fun fetchRating(
    mediaType: MediaType,
    imdbId: String,
  ): Result<TraktRatingApi> = runCatching {
    val url = buildTraktRatingUrl(mediaType = mediaType, imdbId = imdbId)

    client.client.get<TraktRatingApi>(url)
  }
}

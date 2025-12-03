package com.divinelink.core.network.trakt.service

import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.client.TraktClient
import com.divinelink.core.network.client.get
import com.divinelink.core.network.trakt.model.TraktRatingApi
import com.divinelink.core.network.trakt.util.buildTraktRatingUrl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProdTraktService(private val client: TraktClient) : TraktService {

  override fun fetchRating(
    mediaType: MediaType,
    imdbId: String,
  ): Flow<TraktRatingApi> = flow {
    val url = buildTraktRatingUrl(mediaType = mediaType, imdbId = imdbId)

    val response = client.client.get<TraktRatingApi>(url)
    emit(response)
  }
}

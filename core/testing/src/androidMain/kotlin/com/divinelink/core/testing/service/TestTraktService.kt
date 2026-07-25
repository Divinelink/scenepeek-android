package com.divinelink.core.testing.service

import com.divinelink.core.network.trakt.model.TraktRatingApi
import com.divinelink.core.network.trakt.service.TraktService
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestTraktService {
  val mock: TraktService = mock()

  suspend fun mockFetchRating(response: Result<TraktRatingApi>) {
    whenever(mock.fetchRating(mediaType = any(), imdbId = any())).thenReturn(response)
  }
}

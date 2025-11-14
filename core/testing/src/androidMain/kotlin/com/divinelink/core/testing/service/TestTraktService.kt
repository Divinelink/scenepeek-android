package com.divinelink.core.testing.service

import com.divinelink.core.network.trakt.model.TraktRatingApi
import com.divinelink.core.network.trakt.service.TraktService
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestTraktService {
  val mock: TraktService = mock()

  fun mockFetchRating(response: TraktRatingApi) {
    whenever(mock.fetchRating(mediaType = any(), imdbId = any())).thenReturn(flowOf(response))
  }
}

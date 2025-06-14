package com.divinelink.core.testing.service

import com.divinelink.core.network.jellyseerr.model.JellyseerrAccountDetailsResponseApi
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaResponse
import com.divinelink.core.network.jellyseerr.model.movie.JellyseerrMovieDetailsResponse
import com.divinelink.core.network.jellyseerr.model.tv.JellyseerrTvDetailsResponse
import com.divinelink.core.network.jellyseerr.service.JellyseerrService
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestJellyseerrService {
  val mock: JellyseerrService = mock()

  suspend fun mockSignInWithJellyfin(response: Unit) {
    whenever(
      mock.signInWithJellyfin(jellyfinLogin = any()),
    ).thenReturn(
      flowOf(response),
    )
  }

  suspend fun mockSignInWithJellyseerr(response: Unit) {
    whenever(
      mock.signInWithJellyseerr(jellyseerrLogin = any()),
    ).thenReturn(
      flowOf(response),
    )
  }

  suspend fun mockFetchAccountDetails(response: JellyseerrAccountDetailsResponseApi) {
    whenever(
      mock.fetchAccountDetails(address = any()),
    ).thenReturn(
      flowOf(response),
    )
  }

  suspend fun mockLogout(response: Unit) {
    whenever(
      mock.logout(address = any()),
    ).thenReturn(
      flowOf(response),
    )
  }

  suspend fun mockRequestMedia(response: JellyseerrRequestMediaResponse) {
    whenever(
      mock.requestMedia(body = any()),
    ).thenReturn(
      flowOf(response),
    )
  }

  suspend fun mockGetMovieDetails(response: Result<JellyseerrMovieDetailsResponse>) {
    whenever(
      mock.getMovieDetails(any()),
    ).thenReturn(
      flowOf(response),
    )
  }

  suspend fun mockGetTvDetails(response: Result<JellyseerrTvDetailsResponse>) {
    whenever(
      mock.getTvDetails(any()),
    ).thenReturn(
      flowOf(response),
    )
  }
}

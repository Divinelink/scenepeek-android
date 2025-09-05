package com.divinelink.core.testing.service

import com.divinelink.core.network.jellyseerr.model.JellyseerrAccountDetailsResponseApi
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaResponse
import com.divinelink.core.network.jellyseerr.model.MediaInfoRequestResponse
import com.divinelink.core.network.jellyseerr.model.movie.JellyseerrMovieDetailsResponse
import com.divinelink.core.network.jellyseerr.model.server.radarr.RadarrInstanceDetailsResponse
import com.divinelink.core.network.jellyseerr.model.server.radarr.RadarrInstanceResponse
import com.divinelink.core.network.jellyseerr.model.server.sonarr.SonarrInstanceDetailsResponse
import com.divinelink.core.network.jellyseerr.model.server.sonarr.SonarrInstanceResponse
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

  suspend fun mockLogout(response: Result<Unit>) {
    whenever(
      mock.logout(address = any()),
    ).thenReturn(response)
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

  suspend fun mockDeleteRequest(response: Result<Unit>) {
    whenever(mock.deleteRequest(any())).thenReturn(response)
  }

  suspend fun mockDeleteMedia(response: Result<Unit>) {
    whenever(mock.deleteMedia(any())).thenReturn(response)
  }

  suspend fun mockGetRequestDetails(response: Result<MediaInfoRequestResponse>) {
    whenever(mock.getRequestDetails(any())).thenReturn(flowOf(response))
  }

  suspend fun mockGetRadarrInstances(response: Result<List<RadarrInstanceResponse>>) {
    whenever(mock.getRadarrInstances()).thenReturn(response)
  }

  suspend fun mockGetSonarrInstances(response: Result<List<SonarrInstanceResponse>>) {
    whenever(mock.getSonarrInstances()).thenReturn(response)
  }

  suspend fun mockGetRadarrInstanceDetails(response: Result<RadarrInstanceDetailsResponse>) {
    whenever(mock.getRadarrInstanceDetails(any())).thenReturn(response)
  }

  suspend fun mockGetSonarrInstanceDetails(response: Result<SonarrInstanceDetailsResponse>) {
    whenever(mock.getSonarrInstanceDetails(any())).thenReturn(response)
  }
}

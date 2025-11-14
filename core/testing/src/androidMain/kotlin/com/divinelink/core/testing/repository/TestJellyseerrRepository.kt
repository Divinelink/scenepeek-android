package com.divinelink.core.testing.repository

import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.model.jellyseerr.JellyseerrProfile
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.jellyseerr.request.MediaRequestResult
import com.divinelink.core.model.jellyseerr.server.ServerInstance
import com.divinelink.core.model.jellyseerr.server.ServerInstanceDetails
import com.divinelink.core.network.Resource
import com.divinelink.core.network.jellyseerr.model.JellyseerrEditRequestMediaBodyApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class TestJellyseerrRepository {
  val mock: JellyseerrRepository = mock()

  suspend fun mockSignInWithJellyfin(response: Result<Unit>) {
    whenever(mock.signInWithJellyfin(any())).thenReturn(flowOf(response))
  }

  suspend fun mockSignInWithJellyseerr(response: Result<Unit>) {
    whenever(mock.signInWithJellyseerr(any())).thenReturn(flowOf(response))
  }

  suspend fun mockGetJellyseerrProfile(response: Flow<Resource<JellyseerrProfile?>>) {
    whenever(mock.getJellyseerrProfile(any(), any())).thenReturn(response)
  }

  suspend fun verifyGetJellyseerrProfile() {
    verify(mock).getJellyseerrProfile(any(), any())
  }

  suspend fun mockLogout(response: Result<Unit>) {
    whenever(mock.logout(any())).thenReturn(response)
  }

  suspend fun mockRequestMedia(response: Result<MediaRequestResult>) {
    whenever(mock.requestMedia(any())).thenReturn(flowOf(response))
  }

  suspend fun mockRequestDetails(response: Result<JellyseerrRequest>) {
    whenever(mock.getRequestDetails(any())).thenReturn(flowOf(response))
  }

  suspend fun mockDeleteRequest(response: Result<Unit>) {
    whenever(mock.deleteRequest(any())).thenReturn(response)
  }

  suspend fun mockDeleteMedia(response: Result<Unit>) {
    whenever(mock.deleteMedia(any())).thenReturn(response)
  }

  suspend fun mockDeleteFile(response: Result<Unit>) {
    whenever(mock.deleteFile(any())).thenReturn(response)
  }

  suspend fun mockGetMovieDetails(response: JellyseerrMediaInfo?) {
    whenever(mock.getMovieDetails(any())).thenReturn(flowOf(response))
  }

  suspend fun mockGetTvDetails(response: JellyseerrMediaInfo?) {
    whenever(mock.getTvDetails(any())).thenReturn(flowOf(response))
  }

  suspend fun mockGetRadarrInstances(response: Result<List<ServerInstance.Radarr>>) {
    whenever(mock.getRadarrInstances()).thenReturn(response)
  }

  suspend fun mockGetSonarrInstances(response: Result<List<ServerInstance>>) {
    whenever(mock.getSonarrInstances()).thenReturn(response)
  }

  suspend fun mockGetRadarrDetails(response: Result<ServerInstanceDetails>) {
    whenever(mock.getRadarrInstanceDetails(any())).thenReturn(response)
  }

  suspend fun mockGetSonarrDetails(response: Result<ServerInstanceDetails>) {
    whenever(mock.getSonarrInstanceDetails(any())).thenReturn(response)
  }

  suspend fun mockEditRequest(
    request: JellyseerrEditRequestMediaBodyApi,
    response: Result<JellyseerrRequest>,
  ) {
    whenever(mock.editRequest(request)).thenReturn(flowOf(response))
  }

  suspend fun verifyTvDetailsInteractions(times: Int) {
    verify(mock, times(times)).getTvDetails(any())
  }
}

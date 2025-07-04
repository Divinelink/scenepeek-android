package com.divinelink.core.testing.repository

import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.jellyseerr.request.MediaRequestResult
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
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

  fun mockGetLocalJellyseerrAccountDetails(response: JellyseerrAccountDetails?) {
    whenever(mock.getLocalJellyseerrAccountDetails()).thenReturn(flowOf(response))
  }

  suspend fun mockGetRemoteAccountDetails(response: Result<JellyseerrAccountDetails>) {
    whenever(mock.getRemoteAccountDetails(any())).thenReturn(flowOf(response))
  }

  suspend fun verifyClearJellyseerrAccountDetails() {
    verify(mock).clearJellyseerrAccountDetails()
  }

  suspend fun mockLogout(response: Result<Unit>) {
    whenever(mock.logout(any())).thenReturn(flowOf(response))
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
}

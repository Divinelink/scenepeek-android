package com.divinelink.core.testing.repository

import com.divinelink.core.data.jellyseerr.repository.JellyseerrRepository
import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import com.divinelink.core.model.jellyseerr.request.JellyseerrMediaRequest
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class TestJellyseerrRepository {
  val mock: JellyseerrRepository = mock()

  suspend fun mockSignInWithJellyfin(response: Result<JellyseerrAccountDetails>) {
    whenever(mock.signInWithJellyfin(any())).thenReturn(flowOf(response))
  }

  suspend fun mockSignInWithJellyseerr(response: Result<JellyseerrAccountDetails>) {
    whenever(mock.signInWithJellyseerr(any())).thenReturn(flowOf(response))
  }

  fun mockGetJellyseerrAccountDetails(response: JellyseerrAccountDetails) {
    whenever(mock.getJellyseerrAccountDetails()).thenReturn(flowOf(response))
  }

  suspend fun mockInsertJellyseerrAccountDetails() {
    whenever(mock.insertJellyseerrAccountDetails(any())).thenReturn(Unit)
  }

  suspend fun verifyClearJellyseerrAccountDetails() {
    verify(mock).clearJellyseerrAccountDetails()
  }

  suspend fun mockLogout(response: Result<Unit>) {
    whenever(mock.logout(any())).thenReturn(flowOf(response))
  }

  suspend fun mockRequestMedia(response: Result<JellyseerrMediaRequest>) {
    whenever(mock.requestMedia(any())).thenReturn(flowOf(response))
  }
}

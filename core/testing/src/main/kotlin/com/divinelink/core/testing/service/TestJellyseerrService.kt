package com.divinelink.core.testing.service

import com.divinelink.core.network.jellyseerr.model.JellyfinLoginResponseApi
import com.divinelink.core.network.jellyseerr.model.JellyseerrResponseBodyApi
import com.divinelink.core.network.jellyseerr.service.JellyseerrService
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestJellyseerrService {
  val mock: JellyseerrService = mock()

  suspend fun mockSignInWithJellyfin(response: JellyfinLoginResponseApi) {
    whenever(
      mock.signInWithJellyfin(jellyfinLogin = any()),
    ).thenReturn(
      flowOf(response),
    )
  }

  suspend fun mockSignInWithJellyseerr(response: JellyfinLoginResponseApi) {
    whenever(
      mock.signInWithJellyseerr(jellyseerrLogin = any()),
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

  suspend fun mockRequestMedia(response: JellyseerrResponseBodyApi) {
    whenever(
      mock.requestMedia(body = any()),
    ).thenReturn(
      flowOf(response),
    )
  }
}

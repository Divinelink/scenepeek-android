package com.divinelink.scenepeek.fakes.remote

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeRemoteConfig {

  val mock: FirebaseRemoteConfig = mock()

  fun mockFetchAndActivate(response: Boolean) {
    whenever(mock.fetchAndActivate()).thenReturn(mock())

    whenever(mock.fetchAndActivate().isSuccessful).thenReturn(response)

    whenever(mock.fetchAndActivate().isComplete).thenReturn(true)

    whenever(mock.fetchAndActivate().result).thenReturn(true)

    whenever(mock.fetchAndActivate().isCanceled).thenReturn(false)

    whenever(mock.fetchAndActivate().exception).thenReturn(null)
  }

  fun mockException(exception: Exception) {
    whenever(mock.fetchAndActivate()).thenReturn(mock())

    whenever(mock.fetchAndActivate().isSuccessful).thenReturn(true)

    whenever(mock.fetchAndActivate().isComplete).thenReturn(true)

    whenever(mock.fetchAndActivate().result).thenReturn(false)

    whenever(mock.fetchAndActivate().isCanceled).thenReturn(true)

    whenever(mock.fetchAndActivate().exception).thenReturn(exception)
  }

  fun mockGetApiKey(
    key: String,
    response: String,
  ) {
    whenever(mock.getString(key)).thenReturn(response)
  }
}

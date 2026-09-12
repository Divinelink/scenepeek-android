package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.track.MarkEpisodeAsWatchedUseCase
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestMarkEpisodeAsWatchedUseCase {

  val mock: MarkEpisodeAsWatchedUseCase = mock()

  suspend fun mockFailure(exception: Exception = Exception()) {
    whenever(mock.invoke(any())).thenReturn(Result.failure(exception))
  }

  suspend fun mockResponse(response: Result<Unit>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }
}

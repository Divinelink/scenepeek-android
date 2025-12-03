package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.details.media.GetMediaDetailsUseCase
import com.divinelink.core.model.details.media.MediaDetailsResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeGetMediaDetailsUseCase {

  val mock: GetMediaDetailsUseCase = mock()

  fun mockFetchMediaDetails(response: Flow<Result<MediaDetailsResult>>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }

  fun mockFetchMediaDetails(response: Channel<Result<MediaDetailsResult>>) {
    whenever(mock.invoke(any())).thenReturn(response.consumeAsFlow())
  }
}

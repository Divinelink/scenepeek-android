package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.list.FetchListDetailsUseCase
import com.divinelink.core.model.list.ListDetails
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestFetchListDetailsUseCase {

  val mock: FetchListDetailsUseCase = mock()

  init {
    mockFailure()
  }

  private fun mockFailure(exception: Exception = Exception()) {
    whenever(mock.invoke(any())).thenReturn(flowOf(Result.failure(exception)))
  }

  fun mockResponse(response: Result<ListDetails>) {
    whenever(mock.invoke(any())).thenReturn(flowOf(response))
  }

  fun mockResponse(response: Channel<Result<ListDetails>>) {
    whenever(mock.invoke(any())).thenReturn(response.consumeAsFlow())
  }
}

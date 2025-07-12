package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.account.FetchUserListsUseCase
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.list.ListItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestFetchUserListsUseCase {

  val mock: FetchUserListsUseCase = mock()

  init {
    mockFailure()
  }

  private fun mockFailure() {
    whenever(mock.invoke(any())).thenReturn(flowOf(Result.failure(Exception())))
  }

  fun mockResponse(response: Result<PaginationData<ListItem>>) {
    whenever(mock.invoke(any())).thenReturn(flowOf(response))
  }

  fun mockResponse(response: Flow<Result<PaginationData<ListItem>>>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }

  fun mockResponse(response: Channel<Result<PaginationData<ListItem>>>) {
    whenever(mock.invoke(any())).thenReturn(response.consumeAsFlow())
  }
}

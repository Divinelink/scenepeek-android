package com.divinelink.core.testing.usecase

import com.divinelink.core.data.person.details.model.PersonDetailsResult
import com.divinelink.core.domain.details.person.FetchPersonDetailsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestFetchPersonDetailsUseCase {

  val mock: FetchPersonDetailsUseCase = mock()

  init {
    mockFailure()
  }

  private fun mockFailure() {
    whenever(
      mock.invoke(any()),
    ).thenReturn(
      flowOf(Result.failure(Exception())),
    )
  }

  fun mockSuccess(result: PersonDetailsResult) {
    whenever(mock.invoke(any())).thenReturn(
      flowOf(Result.success(result)),
    )
  }

  // Mock multiple emissions
  fun mockSuccess(response: Channel<Result<PersonDetailsResult>>) {
    whenever(mock.invoke(any())).thenReturn(response.consumeAsFlow())
  }
}

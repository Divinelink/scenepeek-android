package com.divinelink.core.testing.usecase.session

import com.divinelink.core.domain.CreateRequestTokenUseCase
import kotlinx.coroutines.test.runTest
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeCreateRequestTokenUseCase {

  val mock: CreateRequestTokenUseCase = mock()

  init {
    runTest {
      mockFailure()
    }
  }

  suspend fun mockFailure() {
    whenever(mock.invoke(any())).thenReturn(Result.failure(Exception()))
  }

  suspend fun mockSuccess(response: Result<String>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }
}

package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.FetchChangesUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestFetchChangesUseCase {

  val mock: FetchChangesUseCase = mock()

  init {
    TestScope().launch {
      mockFailure()
    }
  }

  suspend fun mockFailure() {
    whenever(mock.invoke(any())).thenReturn(Result.failure(Exception()))
  }

  suspend fun mockSuccess() {
    whenever(mock.invoke(any())).thenReturn(Result.success(Unit))
  }
}
